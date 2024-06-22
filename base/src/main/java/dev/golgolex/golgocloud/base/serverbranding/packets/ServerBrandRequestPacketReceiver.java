package dev.golgolex.golgocloud.base.serverbranding.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.serverbranding.packets.ServerBrandRequestPacket;
import dev.golgolex.golgocloud.common.serverbranding.packets.ServerBrandsReplyPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class ServerBrandRequestPacketReceiver extends PacketReceiver<ServerBrandRequestPacket> {
    @Override
    public void receivePacket(ServerBrandRequestPacket serverBrandRequestPacket, NetworkChannel networkChannel) {
        respond(new ServerBrandsReplyPacket(CloudBase.instance().serverBrandingService().loadedBrands()), networkChannel);
    }
}
