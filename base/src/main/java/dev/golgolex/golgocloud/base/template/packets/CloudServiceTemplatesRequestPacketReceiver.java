package dev.golgolex.golgocloud.base.template.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesReplyPacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesRequestPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudServiceTemplatesRequestPacketReceiver extends PacketReceiver<CloudServiceTemplatesRequestPacket> {
    @Override
    public void receivePacket(CloudServiceTemplatesRequestPacket packet, NetworkChannel networkChannel) {
        respond(new CloudServiceTemplatesReplyPacket(CloudBase.instance().templateProvider().cloudServiceTemplates()), networkChannel);
    }
}
