package dev.golgolex.golgocloud.base.network;

import dev.golgolex.golgocloud.base.group.packet.CloudGroupCreatePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupDeletePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupUpdatePacketReceiver;
import dev.golgolex.golgocloud.base.group.packet.CloudGroupsRequestPacketReceiver;
import dev.golgolex.golgocloud.base.instance.packet.InstanceAuthReceiver;
import dev.golgolex.golgocloud.base.instance.packet.InstanceUpdatePacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServiceShutdownPacketReceiver;
import dev.golgolex.golgocloud.base.service.packets.CloudServiceStartedPacketReceiver;
import dev.golgolex.golgocloud.base.template.packets.CloudServiceTemplatesRequestPacketReceiver;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupCreatePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupDeletePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupUpdatePacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupsRequestPacket;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthPacket;
import dev.golgolex.golgocloud.common.instance.packet.InstanceUpdatePacket;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesRequestPacket;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public final class CloudNetworkProviderImpl implements CloudNetworkProvider {

    @Override
    public void initPacketReceivers(@NotNull PacketReceiverManager registry) {
        // instance packets
        registry.registerPacketHandler(InstanceAuthPacket.class, InstanceAuthReceiver.class);
        registry.registerPacketHandler(InstanceUpdatePacket.class, InstanceUpdatePacketReceiver.class);

        // Group packets
        registry.registerPacketHandler(CloudGroupsRequestPacket.class, CloudGroupsRequestPacketReceiver.class);
        registry.registerPacketHandler(CloudGroupUpdatePacket.class, CloudGroupUpdatePacketReceiver.class);
        registry.registerPacketHandler(CloudGroupDeletePacket.class, CloudGroupDeletePacketReceiver.class);
        registry.registerPacketHandler(CloudGroupCreatePacket.class, CloudGroupCreatePacketReceiver.class);

        // template packets
        registry.registerPacketHandler(CloudServiceTemplatesRequestPacket.class, CloudServiceTemplatesRequestPacketReceiver.class);

        // service packets
        registry.registerPacketHandler(CloudServiceStartedPacket.class, CloudServiceStartedPacketReceiver.class);
        registry.registerPacketHandler(CloudServiceShutdownPacket.class, CloudServiceShutdownPacketReceiver.class);
    }
}
