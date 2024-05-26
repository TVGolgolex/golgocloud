package dev.golgolex.golgocloud.base.instance.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceUpdatePacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public final class InstanceUpdatePacketReceiver extends PacketReceiver<CloudInstanceUpdatePacket> {
    @Override
    public void receivePacket(CloudInstanceUpdatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().instanceService().updateInstance(packet.cloudInstance());
    }
}
