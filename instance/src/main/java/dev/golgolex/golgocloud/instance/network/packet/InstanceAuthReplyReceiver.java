package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthReplyPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class InstanceAuthReplyReceiver extends PacketReceiver<InstanceAuthReplyPacket> {
    @Override
    public void receivePacket(InstanceAuthReplyPacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().cloudInstance(packet.cloudInstance());
    }
}
