package dev.golgolex.golgocloud.base.instance.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packet.InstanceUpdatePacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public final class InstanceUpdatePacketReceiver extends PacketReceiver<InstanceUpdatePacket> {
    @Override
    public void receivePacket(InstanceUpdatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().instanceService().update(packet.cloudInstance());
    }
}
