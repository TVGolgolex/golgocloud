package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.service.environment.CloudProxyService;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.common.service.packets.CloudServicePreparePacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.quala.Quala;
import dev.golgolex.quala.utils.color.ConsoleColor;
import dev.golgolex.quala.utils.string.StringUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final List<CloudService> cloudServices = new ArrayList<>();
    private final List<CloudService> waitingService = new ArrayList<>();

    @Override
    public void reloadServices() {
    }

    @Override
    public void updateService(@NotNull CloudService cloudService) {

    }

    @Override
    public void shutdownService(@NotNull CloudService cloudService) {

    }

    public void startedService(@NotNull CloudService cloudService) {
        this.waitingService.removeIf(it -> it.id().equalsIgnoreCase(cloudService.id()));
        this.cloudServices.add(cloudService);
        CloudBase.instance().logger().log(Level.INFO, "Service '" + ConsoleColor.WHITE.ansiCode() + cloudService.id() + ConsoleColor.DEFAULT.ansiCode() + "' started.");
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceStartedPacket(cloudService), null);
    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {
        var group = CloudBase.instance().groupProvider().cloudGroup(cloudService.group());

        group.ifPresentOrElse(cloudGroup -> {
            var instances = CloudBase.instance().instanceService().connectedCloudInstances().stream().filter(cloudInstance -> cloudGroup.instances().contains(cloudInstance.uuid())).toList();

            if (instances.isEmpty()) {
                CloudBase.instance().logger().writeDebug("No instance for group: " + cloudGroup.name() + " connected.");
                return;
            }

            var operatingInstance = instances.size() == 1 ? instances.getFirst() : instances.get(Quala.randomNumber(0, instances.size() - 1));
            if (operatingInstance == null) {
                CloudBase.instance().logger().writeDebug("No instance id for group: " + cloudGroup.name() + " connected.");
                return;
            }

            if (!operatingInstance.ready()) {
                return;
            }

            CloudBase.instance().nettyServer().serverChannelTransmitter().getNetworkChannel(operatingInstance.uuid()).sendPacket(new CloudServicePreparePacket(cloudService));
            this.waitingService.add(cloudService);
            CloudBase.instance().logger().log(Level.INFO, "Service '" + ConsoleColor.WHITE.ansiCode() + cloudService.id() + ConsoleColor.DEFAULT.ansiCode() + "' is prepared.");
        }, () -> CloudBase.instance().logger().log(Level.SEVERE, "No Group for " + cloudService.group() + " found."));
    }

    public CloudService constructService(@NotNull CloudGroup cloudGroup) {
        int id = 1;

        var usedNumbers = this.runningAndWaiting(cloudGroup.name()).stream().map(CloudService::serviceNumber).toList();
        while (usedNumbers.contains(id)) {
            id++;
        }

        switch (cloudGroup.serviceEnvironment()) {
            case SERVER -> {
                return new CloudServerService(
                        cloudGroup.name() + cloudGroup.splitter() + id,
                        id,
                        UUID.randomUUID(),
                        "#" + StringUtils.generateRandomString(12),
                        cloudGroup.name(),
                        cloudGroup.serviceEnvironment(),
                        "",
                        ServicePortDetection.validPort(cloudGroup),
                        false,
                        (cloudGroup.memory() < 0 ? 512 : cloudGroup.memory()),
                        100,
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            }
            case PROXY -> {
                return new CloudProxyService(
                        cloudGroup.name() + cloudGroup.splitter() + id,
                        id,
                        UUID.randomUUID(),
                        "#" + StringUtils.generateRandomString(12),
                        cloudGroup.name(),
                        cloudGroup.serviceEnvironment(),
                        "",
                        ServicePortDetection.validPort(cloudGroup),
                        false,
                        (cloudGroup.memory() < 0 ? 512 : cloudGroup.memory()),
                        100,
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            }
        }
        return null;
    }

    public List<CloudService> runningAndWaiting(@NotNull String group) {
        return new ArrayList<>() {{
            addAll(cloudServices(group));
            addAll(waitingServices(group));
        }};
    }

    public List<CloudService> waitingServices(@NotNull String group) {
        return this.waitingService.stream().filter(cloudService -> cloudService.group().equalsIgnoreCase(group)).toList();
    }
}
