package dev.golgolex.golgocloud.base.service.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceStartedPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudServiceStartedPacketReceiver extends PacketReceiver<CloudServiceStartedPacket> {
    @Override
    public void receivePacket(CloudServiceStartedPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().serviceProvider().startedService(packet.cloudService());
    }
}
