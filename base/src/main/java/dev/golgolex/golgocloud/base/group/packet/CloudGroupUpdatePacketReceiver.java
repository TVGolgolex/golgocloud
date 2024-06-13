package dev.golgolex.golgocloud.base.group.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudGroupUpdatePacketReceiver extends PacketReceiver<CloudGroupUpdatePacket> {
    @Override
    public void receivePacket(CloudGroupUpdatePacket cloudGroup, NetworkChannel networkChannel) {
        CloudBase.instance().groupProvider().updateGroup(cloudGroup.cloudGroup());
    }
}
