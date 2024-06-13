package dev.golgolex.golgocloud.cloudapi.user.packets;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public class CloudPlayerUpdatePacketReceiver extends PacketReceiver<CloudPlayerUpdatePacket> {
    @Override
    public void receivePacket(CloudPlayerUpdatePacket cloudPlayerUpdatePacket, NetworkChannel networkChannel) {
        CloudAPI.instance().cloudPlayerProvider().updateCached(cloudPlayerUpdatePacket.cloudPlayer());
    }
}
