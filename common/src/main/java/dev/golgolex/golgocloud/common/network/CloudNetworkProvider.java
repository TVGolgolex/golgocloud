package dev.golgolex.golgocloud.common.network;

import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public interface CloudNetworkProvider {

    /**
     * Initializes the packet receivers by registering packet handlers in the given registry.
     *
     * @param registry The packet receiver manager used to register packet handlers.
     */
    void initPacketReceivers(@NotNull PacketReceiverManager registry);

}
