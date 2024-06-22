package dev.golgolex.golgocloud.base.instance.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class InstanceUpdatePacketReceiver extends PacketReceiver<CloudInstanceUpdatePacket> {
    @Override
    public void receivePacket(CloudInstanceUpdatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().instanceService().updateInstance(packet.cloudInstance());
    }
}
