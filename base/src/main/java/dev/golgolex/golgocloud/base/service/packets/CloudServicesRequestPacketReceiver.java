package dev.golgolex.golgocloud.base.service.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.packets.CloudServicesReplyPacket;
import dev.golgolex.golgocloud.common.service.packets.CloudServicesRequestPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudServicesRequestPacketReceiver extends PacketReceiver<CloudServicesRequestPacket> {
    @Override
    public void receivePacket(CloudServicesRequestPacket cloudServicesRequestPacket, NetworkChannel networkChannel) {
        respond(new CloudServicesReplyPacket(CloudBase.instance().serviceProvider().cloudServices()), networkChannel);
    }
}
