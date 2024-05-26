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
import java.util.function.Predicate;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final Path runningDynamicServiceDir;
    private final Path staticServiceDir;
    private List<CloudServiceFactory<?>> serviceFactories = new ArrayList<>();
    private List<CloudService> cloudServices = new ArrayList<>();

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
        this.cloudServices = this.resetList(this.cloudServices, it -> it.id().equalsIgnoreCase(cloudService.id()));
        this.serviceFactories = this.resetList(this.serviceFactories, it -> it.cloudService().id().equalsIgnoreCase(cloudService.id()));
        CloudInstance.instance().nettyClient().thisNetworkChannel().sendPacket(new CloudServiceShutdownPacket(cloudService));
    }

    private <T> List<T> resetList(@NotNull List<T> list, Predicate<T> filter) {
        var arrayList = new ArrayList<>(list);
        arrayList.removeIf(filter);
        return arrayList;
    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {
        // no valid operation on instance
    }

    public long usedMemory() {
        var memory = 0L;
        for (var cloudService : this.cloudServices) memory = memory + cloudService.memory();
        return memory;
    }
}
