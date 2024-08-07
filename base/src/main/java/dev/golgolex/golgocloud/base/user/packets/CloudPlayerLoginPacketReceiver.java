package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayerLoginPacketReceiver extends PacketReceiver<CloudPlayerLoginPacket> {
    @Override
    public void receivePacket(CloudPlayerLoginPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().playerProvider().handleLogin(packet.cloudPlayer());
    }
}
