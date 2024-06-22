package dev.golgolex.golgocloud.base.instance.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packets.CloudInstanceAuthPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class InstanceAuthReceiver extends PacketReceiver<CloudInstanceAuthPacket> {
    @Override
    public void receivePacket(CloudInstanceAuthPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().instanceService().connect(packet, networkChannel);
    }
}
