package dev.golgolex.golgocloud.base.service.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudServiceUpdatePacketReceiver extends PacketReceiver<CloudServiceUpdatePacket> {
    @Override
    public void receivePacket(CloudServiceUpdatePacket cloudServiceUpdatePacket, NetworkChannel networkChannel) {
        CloudBase.instance().serviceProvider().updateService(cloudServiceUpdatePacket.cloudService());
    }
}
