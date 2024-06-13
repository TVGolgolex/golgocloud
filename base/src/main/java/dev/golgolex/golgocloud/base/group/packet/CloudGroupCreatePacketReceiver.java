package dev.golgolex.golgocloud.base.group.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupCreatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudGroupCreatePacketReceiver extends PacketReceiver<CloudGroupCreatePacket> {
    @Override
    public void receivePacket(CloudGroupCreatePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().groupProvider().createGroup(packet.cloudGroup());
    }
}
