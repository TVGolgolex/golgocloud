package dev.golgolex.golgocloud.base.instance;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.instance.packet.InstanceAuthPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public final class InstanceAuthReceiver extends PacketReceiver<InstanceAuthPacket> {
    @Override
    public void receivePacket(InstanceAuthPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance().instanceService().connect(packet, networkChannel);
    }
}
