package dev.golgolex.golgocloud.base.service.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.quala.netty5.channel.NetworkChannel;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiver;

public final class CloudServiceShutdownPacketReceiver extends PacketReceiver<CloudServiceShutdownPacket> {
    @Override
    public void receivePacket(CloudServiceShutdownPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance()
                .instanceService()
                .connectedCloudInstances()
                .stream()
                .filter(cloudInstance -> cloudInstance.uuid().equals(networkChannel.channelIdentity().uniqueId()))
                .findFirst()
                .ifPresentOrElse(cloudInstance -> CloudBase.instance().serviceProvider().shutdownService(packet.cloudService()), () -> {
                    CloudBase.instance()
                            .instanceService()
                            .connectedCloudInstances()
                            .stream()
                            .filter(cloudInstance -> cloudInstance.uuid().equals(packet.cloudService().instance()))
                            .findFirst()
                            .ifPresent(cloudInstance -> CloudBase.instance()
                                    .nettyServer()
                                    .serverChannelTransmitter()
                                    .getNetworkChannel(cloudInstance.uuid())
                                    .sendPacket(new CloudServiceShutdownPacket(packet.cloudService())));
                });
    }
}
