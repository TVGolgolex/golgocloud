package dev.golgolex.golgocloud.base.network;

import dev.golgolex.golgocloud.base.configuration.packets.CloudConfigurationRequestPacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupCreatePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupDeletePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupUpdatePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupsRequestPacketReceiver;
import dev.golgolex.golgocloud.base.instance.packet.CloudInstancesRequestPacketReceiver;
import dev.golgolex.golgocloud.base.instance.packet.InstanceAuthReceiver;
import dev.golgolex.golgocloud.base.instance.packet.InstanceUpdatePacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServiceShutdownPacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServiceStartedPacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServiceUpdatePacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServicesRequestPacketReceiver;
import dev.golgolex.golgocloud.base.template.packets.CloudServiceTemplatesRequestPacketReceiver;
import dev.golgolex.golgocloud.base.user.packets.CloudPlayerCreatePacketReceiver;
import dev.golgolex.golgocloud.base.user.packets.CloudPlayerLoginPacketReceiver;
import dev.golgolex.golgocloud.base.user.packets.CloudPlayerLogoutPacketReceiver;
import dev.golgolex.golgocloud.common.configuration.packets.CloudConfigurationRequestPacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupCreatePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupDeletePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupUpdatePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupsRequestPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceUpdatePacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstancesRequestPacket;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServicesRequestPacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesRequestPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCreatePacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLogoutPacket;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public final class CloudNetworkProviderImpl implements CloudNetworkProvider {

    @Override
    public void initPacketReceivers(@NotNull PacketReceiverManager registry) {
        // instance packets
        registry.registerPacketHandler(CloudInstanceAuthPacket.class, InstanceAuthReceiver.class);
        registry.registerPacketHandler(CloudInstanceUpdatePacket.class, InstanceUpdatePacketReceiver.class);
        registry.registerPacketHandler(CloudInstancesRequestPacket.class, CloudInstancesRequestPacketReceiver.class);

        // Group packets
        registry.registerPacketHandler(CloudGroupsRequestPacket.class, CloudGroupsRequestPacketReceiver.class);
        registry.registerPacketHandler(CloudGroupUpdatePacket.class, CloudGroupUpdatePacketReceiver.class);
        registry.registerPacketHandler(CloudGroupDeletePacket.class, CloudGroupDeletePacketReceiver.class);
        registry.registerPacketHandler(CloudGroupCreatePacket.class, CloudGroupCreatePacketReceiver.class);

        // user packets
        registry.registerPacketHandler(CloudPlayerCreatePacket.class, CloudPlayerCreatePacketReceiver.class);
        registry.registerPacketHandler(CloudPlayerLoginPacket.class, CloudPlayerLoginPacketReceiver.class);
        registry.registerPacketHandler(CloudPlayerLogoutPacket.class, CloudPlayerLogoutPacketReceiver.class);

        // template packets
        registry.registerPacketHandler(CloudServiceTemplatesRequestPacket.class, CloudServiceTemplatesRequestPacketReceiver.class);

        // configuration packets
        registry.registerPacketHandler(CloudConfigurationRequestPacket.class, CloudConfigurationRequestPacketReceiver.class);

        // service packets
        registry.registerPacketHandler(CloudServiceStartedPacket.class, CloudServiceStartedPacketReceiver.class);
        registry.registerPacketHandler(CloudServiceShutdownPacket.class, CloudServiceShutdownPacketReceiver.class);
        registry.registerPacketHandler(CloudServicesRequestPacket.class, CloudServicesRequestPacketReceiver.class);
        registry.registerPacketHandler(CloudServiceUpdatePacket.class, CloudServiceUpdatePacketReceiver.class);
    }
}
