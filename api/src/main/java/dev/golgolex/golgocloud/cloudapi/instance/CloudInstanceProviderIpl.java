package dev.golgolex.golgocloud.cloudapi.instance;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.instance.CloudInstance;
import dev.golgolex.golgocloud.common.instance.CloudInstanceProvider;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstancesReplyPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstancesRequestPacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudInstanceProviderIpl implements CloudInstanceProvider {

    private final List<CloudInstance> cloudInstances = new ArrayList<>();

    @Override
    public void reloadInstances() {
        cloudInstances.clear();
        CloudInstancesReplyPacket replyPacket = CloudAPI.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudInstancesRequestPacket());
        cloudInstances.addAll(replyPacket.cloudInstance());
    }

    @Override
    public void updateInstance(@NotNull CloudInstance cloudInstance) {

    }

    @Override
    public void shutdownInstance(@NotNull CloudInstance cloudInstance) {

    }
}
