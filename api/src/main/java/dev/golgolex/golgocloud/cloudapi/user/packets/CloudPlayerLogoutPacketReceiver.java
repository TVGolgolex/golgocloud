package dev.golgolex.golgocloud.cloudapi.user.packets;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.user.events.CloudPlayerLogoutEvent;
import dev.golgolex.golgocloud.common.user.packets.CloudPlayerLogoutPacket;
import dev.golgolex.quala.event.EventRegistry;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudPlayerLogoutPacketReceiver extends PacketReceiver<CloudPlayerLogoutPacket> {
    @Override
    public void receivePacket(CloudPlayerLogoutPacket packet, NetworkChannel networkChannel) {
        EventRegistry.call(new CloudPlayerLogoutEvent(packet.cloudPlayer(), packet.cloudService()));
        CloudAPI.instance().cloudPlayerProvider().updateCached(packet.cloudPlayer());
    }
}
