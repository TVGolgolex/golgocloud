package dev.golgolex.golgocloud.base.group.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupDeletePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudGroupDeletePacketReceiver extends PacketReceiver<CloudGroupDeletePacket> {
    @Override
    public void receivePacket(CloudGroupDeletePacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().groupProvider().deleteGroup(packet.cloudGroup());
    }
}
