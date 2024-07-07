package dev.golgolex.golgocloud.base.user;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.CloudPlayerProvider;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerUpdatePacket;
import dev.golgolex.quala.common.json.JsonDocument;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Accessors(fluent = true)
public final class CloudPlayerProviderImpl implements CloudPlayerProvider {

    private final File databaseDirectory;
    private final List<CloudPlayer> cloudPlayers = new ArrayList<>();
    private final Map<UUID, Long> disconnected = new ConcurrentHashMap<>();

    public CloudPlayerProviderImpl(File baseDirectory) {
        this.databaseDirectory = new File(baseDirectory, "/local/database/cloud-players");
        if (!Files.exists(this.databaseDirectory.toPath())) {
            try {
                Files.createDirectories(this.databaseDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CloudBase.instance().scheduler().runTaskRepeatSync(() -> disconnected.forEach((uuid, aLong) -> {
            if ((aLong + 3000) < System.currentTimeMillis()) {
                this.handleLogout(cloudPlayer(uuid));
            }
        }), 0, 50);
    }

    @Override
    public void reloadPlayers() {
        this.cloudPlayers.clear();
        for (var file : Objects.requireNonNull(this.databaseDirectory.listFiles())) {
            if (file.isFile()) {
                try {
                    CloudPlayer cloudPlayer = JsonDocument.fromFile(file).readObject("cloudPlayer", new TypeToken<CloudPlayer>() {
                    }.getType());

                    // todo check if player is online
                    if (cloudPlayer.onlineCredentials() != null) {
                        cloudPlayer.onlineCredentials(null);
                    }

                    this.cloudPlayers.add(cloudPlayer);
                } catch (Exception exception) {
                    System.err.println(exception.getMessage());
                }
            }
        }
    }

    @Override
    public CloudPlayer cloudPlayer(@NotNull UUID uniqueId) {
        return this.cloudPlayers.stream().filter(cloudPlayer -> cloudPlayer.uniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    @Override
    public CloudPlayer cloudPlayer(@NotNull String name) {
        return this.cloudPlayers.stream().filter(cloudPlayer -> cloudPlayer.username().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void createCloudPlayer(@NotNull CloudPlayer cloudPlayer) {
        this.cloudPlayers.add(cloudPlayer);
        new JsonDocument().write("cloudPlayer", cloudPlayer).saveAsConfig(new File(this.databaseDirectory, cloudPlayer.uniqueId().toString() + ".json").toPath());
    }

    @Override
    public void updateCloudPlayer(@NotNull CloudPlayer cloudPlayer) {
        this.cloudPlayers.removeIf(it -> it.uniqueId().equals(cloudPlayer.uniqueId()));
        this.cloudPlayers.add(cloudPlayer);
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudPlayerUpdatePacket(cloudPlayer), null);
        new JsonDocument().write("cloudPlayer", cloudPlayer).saveAsConfig(new File(this.databaseDirectory, cloudPlayer.uniqueId().toString() + ".json").toPath());
    }

    /**
     * Handles the login of a cloud player by sending a login packet to all server channels.
     *
     * @param cloudPlayer The cloud player to handle login for.
     */
    public void handleLogin(@NotNull CloudPlayer cloudPlayer) {
        CloudBase.instance().logger().info("Player &2'&3" + cloudPlayer.username() + "&2' &1connected to &2'&3" + cloudPlayer.onlineCredentials().currentServer() + "&2'");
        this.updateCloudPlayer(cloudPlayer);
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudPlayerLoginPacket(cloudPlayer), null);
    }

    /**
     * Adds the specified CloudPlayer to the logout queue.
     *
     * @param cloudPlayer The CloudPlayer to be added to the logout queue.
     */
    public void addToLogoutQueue(@NotNull CloudPlayer cloudPlayer) {
        this.disconnected.put(cloudPlayer.uniqueId(), System.currentTimeMillis());
        cloudPlayer.waitingForTransfer(true);
        this.updateCloudPlayer(cloudPlayer);
        CloudBase.instance().logger().info("Player &2'&3" + cloudPlayer.username() + "&2' &1disconnected from &2'&3" + cloudPlayer.onlineCredentials().currentServer() + "&2' (&1added to queue &33 seconds&2)");
    }

    /**
     * Handles the logout process for a cloud player.
     *
     * @param cloudPlayer The cloud player to handle the logout for.
     */
    public void handleLogout(@NotNull CloudPlayer cloudPlayer) {
        disconnected.remove(cloudPlayer.uniqueId());
        if (cloudPlayer.onlineCredentials() != null) {
            CloudBase.instance().logger().info("Player &2'&3" + cloudPlayer.username() + "&2' &1disconnected from &2'&3" + cloudPlayer.onlineCredentials().currentServer() + "&2'");
        } else {
            CloudBase.instance().logger().warn("Player &2'&3" + cloudPlayer.username() + "&2' &1has no OnlineCredentials");
        }
        cloudPlayer.onlineCredentials(null);
        this.updateCloudPlayer(cloudPlayer);
    }

    @Override
    public void updateCached(@NotNull CloudPlayer element) {

    }

    @Override
    public void putCache(@NotNull CloudPlayer element) {

    }

    @Override
    public void unCache(@NotNull CloudPlayer element) {

    }
}
