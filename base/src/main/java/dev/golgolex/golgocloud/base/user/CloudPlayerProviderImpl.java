package dev.golgolex.golgocloud.base.user;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.CloudPlayerProvider;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.quala.json.document.JsonDocument;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public class CloudPlayerProviderImpl implements CloudPlayerProvider {

    private final File databaseDirectory;
    private final List<CloudPlayer> cloudPlayers = new ArrayList<>();

    public CloudPlayerProviderImpl(File baseDirectory) {
        this.databaseDirectory = new File(baseDirectory, "/local/database/cloud-players");
        if (!Files.exists(this.databaseDirectory.toPath())) {
            try {
                Files.createDirectories(this.databaseDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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
        return null;
    }

    @Override
    public CloudPlayer cloudPlayer(@NotNull String name) {
        return null;
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
