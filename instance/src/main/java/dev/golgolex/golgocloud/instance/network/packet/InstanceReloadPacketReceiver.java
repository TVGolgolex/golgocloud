package dev.golgolex.golgocloud.instance.network.packet;

import dev.golgolex.golgocloud.common.instance.packet.CloudInstanceReloadPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class InstanceReloadPacketReceiver extends PacketReceiver<CloudInstanceReloadPacket> {
    @Override
    public void receivePacket(CloudInstanceReloadPacket cloudInstanceReloadPacket, NetworkChannel networkChannel) {
        CloudInstance.instance().reload();
    }
}
