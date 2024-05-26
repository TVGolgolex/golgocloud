package dev.golgolex.golgocloud.cloudapi.group;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import dev.golgolex.golgocloud.common.group.packets.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class CloudGroupProviderImpl implements CloudGroupProvider {

    private final List<CloudGroup> cloudGroups = new ArrayList<>();

    @Override
    public void reloadGroups() {
        this.cloudGroups.clear();
        final CloudGroupsReplyPacket reply = CloudAPI.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudGroupsRequestPacket());
        this.cloudGroups.addAll(reply.cloudGroups());
        if (!this.cloudGroups.isEmpty()) {
            CloudAPI.instance().logger().log(Level.INFO, "Loaded following groups: " + this.cloudGroups.stream()
                    .map(CloudGroup::name)
                    .collect(Collectors.joining(", ")));
        }
    }

    @Override
    public void updateGroup(@NotNull CloudGroup cloudGroup) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupUpdatePacket(cloudGroup));
    }

    @Override
    public void deleteGroup(@NotNull CloudGroup cloudGroup) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupDeletePacket(cloudGroup));
    }

    @Override
    public void createGroup(@NotNull CloudGroup cloudGroup) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupCreatePacket(cloudGroup));
    }
}
