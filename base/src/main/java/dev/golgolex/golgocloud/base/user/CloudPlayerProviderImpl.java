package dev.golgolex.golgocloud.base.user;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.CloudPlayerProvider;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLogoutPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerUpdatePacket;
import dev.golgolex.quala.json.document.JsonDocument;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public class CloudPlayerProviderImpl implements CloudPlayerProvider {

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
        new JsonDocument().write("cloudPlayer", cloudPlayer).saveAsConfig(new File(this.databaseDirectory, cloudPlayer.uniqueId().toString() + ".json").toPath());
    }

    public void handleLogin(@NotNull CloudPlayer cloudPlayer) {
        CloudBase.instance().logger().log(Level.INFO, "Player '" + cloudPlayer.names() + "' connected to " + cloudPlayer.onlineCredentials().currentServer() + "'");
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudPlayerLoginPacket(cloudPlayer), null);
    }

    public void handleLogout(@NotNull CloudPlayer cloudPlayer) {
        CloudBase.instance().logger().log(Level.INFO, "Player '" + cloudPlayer.names() + "' disconnected from " + cloudPlayer.onlineCredentials().currentServer() + "'");
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudPlayerLogoutPacket(cloudPlayer, CloudBase.instance().serviceProvider().cloudService(cloudPlayer.onlineCredentials().currentServer()).orElse(null)), null);
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
