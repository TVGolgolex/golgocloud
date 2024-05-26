package dev.golgolex.golgocloud.cloudapi.service;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServicesReplyPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServicesRequestPacket;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class CloudServiceProviderImpl implements CloudServiceProvider {

    private final List<CloudService> cloudServices = new ArrayList<>();

    @Override
    public void reloadServices() {
        this.cloudServices.clear();
        CloudServicesReplyPacket replyPacket = CloudAPI.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudServicesRequestPacket());
        this.cloudServices.addAll(replyPacket.cloudService());
        if (!this.cloudServices.isEmpty()) {
            CloudAPI.instance().logger().log(Level.INFO, "Loaded following services: " + this.cloudServices.stream()
                    .map(cloudService -> cloudService.id() + "#" + cloudService.uuid().toString())
                    .collect(Collectors.joining(", ")));
        }
    }

    @Override
    public void updateService(@NotNull CloudService cloudService) {
        CloudAPI.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudServiceUpdatePacket(cloudService));
    }

    @Override
    public void shutdownService(@NotNull CloudService cloudService) {

    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {

    }
}
