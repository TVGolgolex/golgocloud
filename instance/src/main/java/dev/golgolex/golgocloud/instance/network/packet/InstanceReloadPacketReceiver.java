package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.InstanceReloadPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class InstanceReloadPacketReceiver extends PacketReceiver<InstanceReloadPacket> {
    @Override
    public void receivePacket(InstanceReloadPacket instanceReloadPacket, NetworkChannel networkChannel) {
        CloudInstance.instance().reload();
    }
}
