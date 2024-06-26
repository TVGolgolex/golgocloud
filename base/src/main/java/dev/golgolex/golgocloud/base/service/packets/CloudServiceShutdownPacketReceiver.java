package dev.golgolex.golgocloud.base.service.packets;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceShutdownPacket;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

public final class CloudServiceShutdownPacketReceiver extends PacketReceiver<CloudServiceShutdownPacket> {
    @Override
    public void receivePacket(CloudServiceShutdownPacket packet, NetworkChannel networkChannel) {
        CloudBase.instance()
                .instanceService()
                .cloudInstances()
                .stream()
                .filter(cloudInstance -> cloudInstance.uuid().equals(networkChannel.channelIdentity().uniqueId()))
                .findFirst()
                .ifPresentOrElse(cloudInstance -> CloudBase.instance().serviceProvider().shutdownService(packet.cloudService()), () -> CloudBase.instance()
                        .instanceService()
                        .cloudInstances()
                        .stream()
                        .filter(cloudInstance -> cloudInstance.uuid().equals(packet.cloudService().uuid()))
                        .findFirst()
                        .ifPresent(cloudInstance -> CloudBase.instance().nettyServer().serverChannelTransmitter().getNetworkChannel(cloudInstance.uuid()).sendPacket(new CloudServiceShutdownPacket(packet.cloudService()))));
    }
}
