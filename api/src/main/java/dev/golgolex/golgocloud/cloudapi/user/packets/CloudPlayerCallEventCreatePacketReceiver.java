package dev.golgolex.golgocloud.cloudapi.user.packets;

import dev.golgolex.golgocloud.common.user.events.CloudPlayerCreateEvent;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerCallEventCreatePacket;
import dev.golgolex.quala.event.registry.EventRegistry;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudPlayerCallEventCreatePacketReceiver extends PacketReceiver<CloudPlayerCallEventCreatePacket> {
    @Override
    public void receivePacket(CloudPlayerCallEventCreatePacket cloudPlayerCallEventCreatePacket, NetworkChannel networkChannel) {
        EventRegistry.call(new CloudPlayerCreateEvent(cloudPlayerCallEventCreatePacket.cloudPlayer()));
    }
}
