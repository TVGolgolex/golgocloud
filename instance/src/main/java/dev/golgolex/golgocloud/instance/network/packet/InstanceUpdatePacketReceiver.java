package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.InstanceUpdatePacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class InstanceUpdatePacketReceiver extends PacketReceiver<InstanceUpdatePacket> {
    @Override
    public void receivePacket(InstanceUpdatePacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().cloudInstance(packet.cloudInstance());
    }
}