package dev.golgolex.golgocloud.instance.service.packet;

import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.common.service.packets.CloudServicePreparePacket;
import dev.golgolex.golgocloud.instance.service.CloudServerServiceFactory;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public class CloudServicePreparePacketReceiver extends PacketReceiver<CloudServicePreparePacket> {
    @Override
    public void receivePacket(CloudServicePreparePacket packet, NetworkChannel networkChannel) {
        switch (packet.cloudService().environment()) {
            case PROXY -> {
            }
            case SERVER -> {
                var factory = new CloudServerServiceFactory((CloudServerService) packet.cloudService());
                factory.prepare();
            }
        }
    }
}
