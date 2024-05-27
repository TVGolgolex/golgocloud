package dev.golgolex.golgocloud.cloudapi.network;

import dev.golgolex.golgocloud.cloudapi.service.packets.CloudServiceUpdatePacketReceiver;
import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public class CloudNetworkProviderImpl implements CloudNetworkProvider {
    @Override
    public void initPacketReceivers(@NotNull PacketReceiverManager registry) {
        // service packets
        registry.registerPacketHandler(CloudServiceUpdatePacket.class, CloudServiceUpdatePacketReceiver.class);
    }
}
