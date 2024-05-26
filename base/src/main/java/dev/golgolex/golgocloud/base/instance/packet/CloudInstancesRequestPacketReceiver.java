package dev.golgolex.golgocloud.base.instance.packet;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstancesReplyPacket;
import dev.golgolex.golgocloud.common.instance.packet.CloudInstancesRequestPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudInstancesRequestPacketReceiver extends PacketReceiver<CloudInstancesRequestPacket> {
    @Override
    public void receivePacket(CloudInstancesRequestPacket cloudInstancesRequestPacket, NetworkChannel networkChannel) {
        respond(new CloudInstancesReplyPacket(CloudBase.instance().instanceService().cloudInstances()), networkChannel);
    }
}
