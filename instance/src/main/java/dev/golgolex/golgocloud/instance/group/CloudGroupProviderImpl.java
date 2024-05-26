package dev.golgolex.golgocloud.instance.group;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import dev.golgolex.golgocloud.common.group.packets.*;
import dev.golgolex.golgocloud.instance.CloudInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public final class CloudGroupProviderImpl implements CloudGroupProvider {

    private final List<CloudGroup> cloudGroups = new ArrayList<>();

    @Override
    public void reloadGroups() {
        this.cloudGroups.clear();
        final CloudGroupsReplyPacket reply = CloudInstance.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudGroupsRequestPacket());
        this.cloudGroups.addAll(reply.cloudGroups().stream().filter(cloudGroup -> cloudGroup.instances().contains(CloudInstance.instance().instanceId())).toList());
        if (!this.cloudGroups.isEmpty()) {
            CloudInstance.instance().logger().log(Level.INFO, "Loaded following groups: " + this.cloudGroups.stream()
                    .map(CloudGroup::name)
                    .collect(Collectors.joining(", ")));
        }
    }

    @Override
    public void updateGroup(@NotNull CloudGroup cloudGroup) {
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupUpdatePacket(cloudGroup));
    }

    @Override
    public void deleteGroup(@NotNull CloudGroup cloudGroup) {
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupDeletePacket(cloudGroup));
    }

    @Override
    public void createGroup(@NotNull CloudGroup cloudGroup) {
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudGroupCreatePacket(cloudGroup));
    }
}
