package dev.golgolex.golgocloud.instance.network;

import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthReplyPacket;
import dev.golgolex.golgocloud.instance.network.packet.InstanceAuthReplyReceiver;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;

public class NetworkManager {

    public void initPacketReceivers(PacketReceiverManager registry) {
        // instance handling
        registry.registerPacketHandler(InstanceAuthReplyPacket.class, InstanceAuthReplyReceiver.class);
    }

}
