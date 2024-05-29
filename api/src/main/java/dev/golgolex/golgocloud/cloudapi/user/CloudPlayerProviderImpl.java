package dev.golgolex.golgocloud.cloudapi.user;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.golgocloud.common.user.CloudPlayerProvider;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCreatePacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
public class CloudPlayerProviderImpl implements CloudPlayerProvider {

    private final List<CloudPlayer> cloudPlayers = new ArrayList<>();

    @Override
    public void reloadPlayers() {

    }

    @Override
    public CloudPlayer cloudPlayer(@NotNull UUID uniqueId) {
        return cloudPlayers.stream().filter(cloudPlayer -> cloudPlayer.uniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    @Override
    public CloudPlayer cloudPlayer(@NotNull String name) {
        return cloudPlayers.stream().filter(cloudPlayer -> cloudPlayer.username().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void createCloudPlayer(@NotNull CloudPlayer cloudPlayer) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudPlayerCreatePacket(cloudPlayer));
    }
}
