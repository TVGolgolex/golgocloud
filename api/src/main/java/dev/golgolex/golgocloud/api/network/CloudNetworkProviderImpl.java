package dev.golgolex.golgocloud.api.network;

import dev.golgolex.golgocloud.common.network.CloudNetworkProvider;
import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public class CloudNetworkProviderImpl implements CloudNetworkProvider {
    @Override
    public void initPacketReceivers(@NotNull PacketReceiverManager registry) {

    }
}
