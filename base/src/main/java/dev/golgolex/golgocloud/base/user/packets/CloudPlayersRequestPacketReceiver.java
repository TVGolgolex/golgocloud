package dev.golgolex.golgocloud.base.user.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayersReplyPacket;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayersRequestPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayersRequestPacketReceiver extends PacketReceiver<CloudPlayersRequestPacket> {
    @Override
    public void receivePacket(CloudPlayersRequestPacket cloudPlayersRequestPacket, NetworkChannel networkChannel) {
        respond(new CloudPlayersReplyPacket(CloudBase.instance().playerProvider().cloudPlayers()), networkChannel);
    }
}
