package dev.golgolex.golgocloud.common.network.packets;

/*
 * Copyright 2023-2024 golgocloud contributors
 */

import dev.golgolex.golgocloud.common.network.CloudNetworkUpdateType;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class CloudNetworkReloadPacket extends Packet {

    private final CloudNetworkUpdateType type;

    public CloudNetworkReloadPacket(@NotNull CloudNetworkUpdateType type)
    {
        this.type = type;
        buffer.writeEnum(this.type);
    }

    public CloudNetworkReloadPacket(CodecBuffer buffer)
    {
        super(buffer);
        type = buffer.readEnum(CloudNetworkUpdateType.class);
    }
}
