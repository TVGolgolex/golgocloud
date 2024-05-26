package dev.golgolex.golgocloud.common.network;

import dev.golgolex.quala.netty5.protocol.receiver.PacketReceiverManager;
import org.jetbrains.annotations.NotNull;

public interface CloudNetworkProvider {

    void initPacketReceivers(@NotNull PacketReceiverManager registry);

}
