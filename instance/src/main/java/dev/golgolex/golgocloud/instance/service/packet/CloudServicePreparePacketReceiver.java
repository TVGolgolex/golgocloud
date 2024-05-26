package dev.golgolex.golgocloud.instance.service.packet;

import dev.golgolex.golgocloud.common.service.packets.CloudServicePreparePacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public final class CloudServicePreparePacketReceiver extends PacketReceiver<CloudServicePreparePacket> {
    @Override
    public void receivePacket(CloudServicePreparePacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().serverProcessQueue().put(packet.cloudService());
    }
}
