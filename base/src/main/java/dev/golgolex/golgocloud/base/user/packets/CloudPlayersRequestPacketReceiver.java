package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayersReplyPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayersRequestPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudPlayersRequestPacketReceiver extends PacketReceiver<CloudPlayersRequestPacket> {
    @Override
    public void receivePacket(CloudPlayersRequestPacket cloudPlayersRequestPacket, NetworkChannel networkChannel) {
        respond(new CloudPlayersReplyPacket(CloudBase.instance().playerProvider().cloudPlayers()), networkChannel);
    }
}
