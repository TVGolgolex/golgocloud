package dev.golgolex.golgocloud.base.group.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupsReplyPacket;
import dev.golgolex.golgocloud.common.group.packets.CloudGroupsRequestPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

import java.util.ArrayList;

public final class CloudGroupsRequestPacketReceiver extends PacketReceiver<CloudGroupsRequestPacket> {
    @Override
    public void receivePacket(CloudGroupsRequestPacket cloudGroupsRequestPacket, NetworkChannel networkChannel) {
        var list = new ArrayList<CloudGroup>();
        if (cloudGroupsRequestPacket.serviceEnvironment() == null) {
            list.addAll(CloudBase.instance().groupProvider().cloudGroups());
        } else {
            list.addAll(CloudBase.instance().groupProvider().cloudGroups(cloudGroupsRequestPacket.serviceEnvironment()));
        }
        respond(new CloudGroupsReplyPacket(list), networkChannel);
    }
}
