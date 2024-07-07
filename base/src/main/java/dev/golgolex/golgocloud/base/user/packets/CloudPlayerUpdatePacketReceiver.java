package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayerUpdatePacketReceiver extends PacketReceiver<CloudPlayerUpdatePacket> {
    @Override
    public void receivePacket(CloudPlayerUpdatePacket cloudPlayerUpdatePacket, NetworkChannel networkChannel) {
        CloudBase.instance().playerProvider().updateCloudPlayer(cloudPlayerUpdatePacket.cloudPlayer());
    }
}
