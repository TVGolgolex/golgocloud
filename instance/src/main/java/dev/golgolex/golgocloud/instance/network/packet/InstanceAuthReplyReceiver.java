package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceAuthReplyPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public class InstanceAuthReplyReceiver extends PacketReceiver<CloudInstanceAuthReplyPacket> {
    @Override
    public void receivePacket(CloudInstanceAuthReplyPacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().cloudInstance(packet.cloudInstance());
    }
}
