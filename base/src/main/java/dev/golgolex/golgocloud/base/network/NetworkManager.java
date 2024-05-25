package dev.golgolex.golgocloud.base.network;

import dev.golgolex.golgocloud.base.instance.InstanceAuthReceiver;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthPacket;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;

public class NetworkManager {

    public void initPacketReceivers(PacketReceiverManager registry) {
        registry.registerPacketHandler(InstanceAuthPacket.class, InstanceAuthReceiver.class);
    }

}
