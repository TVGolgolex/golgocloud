package dev.golgolex.golgocloud.cloudapi.user.packets;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLoginPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public class CloudPlayerLoginPacketReceiver extends PacketReceiver<CloudPlayerLoginPacket> {
    @Override
    public void receivePacket(CloudPlayerLoginPacket packet, NetworkChannel networkChannel) {
        CloudAPI.instance().cloudPlayerProvider().updateCached(packet.cloudPlayer());
    }
}
