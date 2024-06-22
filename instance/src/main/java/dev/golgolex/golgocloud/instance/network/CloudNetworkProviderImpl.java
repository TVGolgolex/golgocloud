package dev.golgolex.golgocloud.instance.network;

import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceAuthReplyPacket;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceReloadPacket;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceUpdatePacket;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServicePreparePacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.instance.network.packet.InstanceAuthReplyReceiver;
import dev.golgolex.golgocloud.instance.network.packet.InstanceReloadPacketReceiver;
import dev.golgolex.golgocloud.instance.network.packet.InstanceUpdatePacketReceiver;
import dev.golgolex.golgocloud.instance.service.packets.CloudServicePreparePacketReceiver;
import dev.golgolex.golgocloud.instance.service.packets.CloudServiceShutdownPacketReceiver;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public final class CloudNetworkProviderImpl implements CloudNetworkProvider {

    @Override
    public void initPacketReceivers(@NotNull PacketReceiverManager registry) {
        // instance packets
        registry.registerPacketHandler(CloudInstanceAuthReplyPacket.class, InstanceAuthReplyReceiver.class);
        registry.registerPacketHandler(CloudInstanceUpdatePacket.class, InstanceUpdatePacketReceiver.class);
        registry.registerPacketHandler(CloudInstanceReloadPacket.class, InstanceReloadPacketReceiver.class);

        // service packets
        registry.registerPacketHandler(CloudServicePreparePacket.class, CloudServicePreparePacketReceiver.class);
        registry.registerPacketHandler(CloudServiceShutdownPacket.class, CloudServiceShutdownPacketReceiver.class);
    }
}
