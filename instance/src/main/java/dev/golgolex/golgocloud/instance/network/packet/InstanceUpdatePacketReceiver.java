package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceUpdatePacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class InstanceUpdatePacketReceiver extends PacketReceiver<CloudInstanceUpdatePacket> {
    @Override
    public void receivePacket(CloudInstanceUpdatePacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().cloudInstance(packet.cloudInstance());
    }
}