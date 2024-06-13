package dev.golgolex.golgocloud.base.configuration.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.configuration.packets.CloudConfigurationReplyPacket;
import dev.golgolex.golgocloud.common.configuration.packets.CloudConfigurationRequestPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public class CloudConfigurationRequestPacketReceiver extends PacketReceiver<CloudConfigurationRequestPacket> {
    @Override
    public void receivePacket(CloudConfigurationRequestPacket cloudConfigurationRequestPacket, NetworkChannel networkChannel) {
        var config = CloudBase.instance().configurationService().configuration(cloudConfigurationRequestPacket.configuration());
        respond(new CloudConfigurationReplyPacket(
                config == null ? null : config.configuration()
        ), networkChannel);
    }
}
