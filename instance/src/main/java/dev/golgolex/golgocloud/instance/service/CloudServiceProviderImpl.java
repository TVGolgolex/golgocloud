package dev.golgolex.golgocloud.instance.service;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.CloudServiceFactory;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final List<CloudServiceFactory<?>> serviceFactories = new ArrayList<>();
    private final List<CloudService> cloudServices = new ArrayList<>();
    private final Path runningDynamicServiceDir;
    private final Path staticServiceDir;

    public CloudServiceProviderImpl(@NotNull File instanceDirectory) {
        this.runningDynamicServiceDir = new File(instanceDirectory, "/running/dynamic/").toPath();
        this.staticServiceDir = new File(instanceDirectory, "/running/static/").toPath();
    }

    @Override
    public void reloadServices() {

    }

    @Override
    public void updateService(@NotNull CloudService cloudService) {
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudServiceUpdatePacket(cloudService));
    }

    @Override
    public void shutdownService(@NotNull CloudService cloudService) {
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudServiceShutdownPacket(cloudService));
    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {
        // no valid operation on instance
    }
}
