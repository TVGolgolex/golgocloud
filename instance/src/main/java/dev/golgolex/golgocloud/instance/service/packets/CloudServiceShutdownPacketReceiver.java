package dev.golgolex.golgocloud.instance.service.packets;

import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudServiceShutdownPacketReceiver extends PacketReceiver<CloudServiceShutdownPacket> {
    @Override
    public void receivePacket(CloudServiceShutdownPacket packet, NetworkChannel networkChannel) {
        CloudInstance.instance().serviceProvider()
                .serviceFactories()
                .stream()
                .filter(cloudServiceFactory -> cloudServiceFactory.cloudService().uuid().equals(packet.cloudService().uuid()))
                .findFirst()
                .ifPresent(cloudServiceFactory -> {
                    cloudServiceFactory.terminate();
                    CloudInstance.instance().serviceProvider().cloudServices().removeIf(cloudService -> cloudService.uuid().equals(packet.cloudService().uuid()));
                });
    }
}
