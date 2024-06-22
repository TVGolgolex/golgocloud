package dev.golgolex.golgocloud.common.serverbranding.packets;

import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class AbstractServerBrandPacket extends Packet {

    private final ServerBrandStyle serverBrandStyle;

    public AbstractServerBrandPacket(CodecBuffer buffer) {
        super(buffer);
        this.serverBrandStyle = buffer.readBufferClass(new ServerBrandStyle());
    }

    public AbstractServerBrandPacket(ServerBrandStyle serverBrandStyle) {
        this.serverBrandStyle = serverBrandStyle;
         buffer.writeBufferClass(serverBrandStyle);
    }
}
