package dev.golgolex.golgocloud.cloudapi.service.packets;

/*
 * Copyright 2023-2024 golgocloud contributors
 */

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.service.events.CloudServiceUpdateEvent;
import dev.golgolex.golgocloud.common.service.packets.CloudServiceUpdatePacket;
import dev.golgolex.quala.event.registry.EventRegistry;
import dev.golgolex.quala.netty5.basic.channel.NetworkChannel;
import dev.golgolex.quala.netty5.basic.protocol.receiver.PacketReceiver;

import java.util.ArrayList;

public final class CloudServiceUpdatePacketReceiver extends PacketReceiver<CloudServiceUpdatePacket> {
    @Override
    public void receivePacket(CloudServiceUpdatePacket packet, NetworkChannel networkChannel) {
        EventRegistry.call(new CloudServiceUpdateEvent(packet.cloudService()));
        var runningServices = new ArrayList<>(new ArrayList<>(CloudAPI.instance().cloudServiceProvider().cloudServices())
                .stream()
                .filter(cloudService -> !cloudService.id().equals(packet.cloudService().id()))
                .toList());
        runningServices.add(packet.cloudService());
        CloudAPI.instance().cloudServiceProvider().cloudServices().clear();
        CloudAPI.instance().cloudServiceProvider().cloudServices().addAll(runningServices);
    }
}
