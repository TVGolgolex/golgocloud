package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import dev.golgolex.golgocloud.common.service.ServerState;
import dev.golgolex.golgocloud.common.service.ServiceLifeCycle;
import dev.golgolex.golgocloud.common.service.environment.CloudProxyService;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.common.service.packets.CloudServicePreparePacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.common.Quala;
import dev.golgolex.quala.common.utils.string.StringUtils;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.logging.Level;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private List<CloudService> cloudServices = new ArrayList<>();
    private List<CloudService> waitingService = new ArrayList<>();

    @Override
    public void reloadServices() {
    }

    @Override
    public void updateService(@NotNull CloudService cloudService) {
        this.cloudServices = this.resetList(this.cloudServices, it -> it.id().equalsIgnoreCase(cloudService.id()));
        this.cloudServices.add(cloudService);
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceUpdatePacket(cloudService), null);
    }

    @Override
    public void shutdownService(@NotNull CloudService cloudService) {
        CloudBase.instance().logger().info("Service &2'&3" + cloudService.id() + "&2' &1was stopped");
        this.cloudServices = this.resetList(this.cloudServices, it -> it.id().equalsIgnoreCase(cloudService.id()));

        for (var cloudPlayer : CloudBase.instance().playerProvider().cloudPlayers()) {
            if (cloudPlayer.onlineCredentials() != null
                    && cloudPlayer.onlineCredentials().currentServer() != null
                    && cloudPlayer.onlineCredentials().currentServer().equalsIgnoreCase(cloudService.id())) {
                CloudBase.instance().playerProvider().addToLogoutQueue(cloudPlayer);
            }
        }

        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceShutdownPacket(cloudService), networkChannel -> networkChannel.channelIdentity().uniqueId().equals(cloudService.uuid()));
    }

    public void startedService(@NotNull CloudService cloudService) {
        this.waitingService = this.resetList(this.waitingService, it -> it.id().equalsIgnoreCase(cloudService.id()));
        this.cloudServices.add(cloudService);
        CloudBase.instance().logger().info("Service &2'&3" + cloudService.id() + "&2' &1was started");
        CloudBase.instance().nettyServer().serverChannelTransmitter().sendPacketToAll(new CloudServiceStartedPacket(cloudService), null);
    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {
        CloudBase.instance().groupProvider().cloudGroup(cloudService.group()).ifPresentOrElse(cloudGroup -> {
            var instances = CloudBase.instance().instanceService().cloudInstances().stream().filter(cloudInstance -> cloudGroup.instances().contains(cloudInstance.uuid())).toList();

            if (instances.isEmpty()) {
                CloudBase.instance().logger().debug("No instance for group: " + cloudGroup.name() + " connected.");
                return;
            }

            var operatingInstance = instances.size() == 1 ? instances.getFirst() : instances.get(Quala.randomNumber(0, instances.size() - 1));
            if (operatingInstance == null) {
                CloudBase.instance().logger().debug("No instance id for group: " + cloudGroup.name() + " connected.");
                return;
            }

            if (!operatingInstance.ready()) {
                return;
            }

            cloudService.instance(operatingInstance.uuid());
            CloudBase.instance().nettyServer().serverChannelTransmitter().getNetworkChannel(operatingInstance.uuid()).sendPacket(new CloudServicePreparePacket(cloudService));
            this.waitingService.add(cloudService);
            CloudBase.instance().logger().info("Service &2'&3" + cloudService.id() + "&2' &1was prepared");
        }, () -> CloudBase.instance().logger().error("No Group for " + cloudService.group() + " found.", null));
    }

    public CloudService constructService(@NotNull CloudGroup cloudGroup) {
        int id = 1;

        var usedNumbers = this.runningAndWaiting(cloudGroup.name()).stream().map(CloudService::serviceNumber).toList();
        while (usedNumbers.contains(id)) {
            id++;
        }

        var templates = CloudBase.instance().templateProvider().cloudServiceTemplates(cloudGroup.name());

        CloudServiceTemplate template;
        if (templates.isEmpty()) {
            template = new CloudServiceTemplate(
                    cloudGroup.name(),
                    "default",
                    true,
                    cloudGroup.instances()
            );
            CloudBase.instance().templateProvider().createCloudServiceTemplate(template);
        } else {
            template = templates.get(Quala.randomNumber(templates.size()));
        }


        switch (cloudGroup.serviceEnvironment()) {
            case SERVER -> {
                return new CloudServerService(
                        cloudGroup.name() + cloudGroup.splitter() + id,
                        id,
                        UUID.randomUUID(),
                        "#" + StringUtils.generateRandomString(12),
                        cloudGroup.serviceEnvironment(),
                        ServiceLifeCycle.PREPARED,
                        cloudGroup.name(),
                        null,
                        template.id(),
                        "",
                        "",
                        ServicePortDetection.validPort(cloudGroup),
                        false,
                        (cloudGroup.memory() < 0 ? 512 : cloudGroup.memory()),
                        100,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        ServerState.NORMAL
                );
            }
            case PROXY -> {
                return new CloudProxyService(
                        cloudGroup.name() + cloudGroup.splitter() + id,
                        id,
                        UUID.randomUUID(),
                        "#" + StringUtils.generateRandomString(12),
                        cloudGroup.serviceEnvironment(),
                        ServiceLifeCycle.PREPARED,
                        cloudGroup.name(),
                        null,
                        template.id(),
                        "",
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

    private <T> List<T> resetList(@NotNull List<T> list, Predicate<T> filter) {
        var arrayList = new ArrayList<>(list);
        arrayList.removeIf(filter);
        return arrayList;
    }
}
