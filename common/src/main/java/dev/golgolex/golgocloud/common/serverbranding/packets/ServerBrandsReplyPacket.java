package dev.golgolex.golgocloud.common.serverbranding.packets;

import dev.golgolex.golgocloud.common.serverbranding.ServerBrandStyle;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class ServerBrandsReplyPacket extends Packet {

    private final List<ServerBrandStyle> serverBrandStyles = new ArrayList<>();

    public ServerBrandsReplyPacket(List<ServerBrandStyle> serverBrandStyles) {
        this.serverBrandStyles.addAll(serverBrandStyles);
        buffer.writeList(serverBrandStyles, CodecBuffer::writeBufferClass);
    }

    public ServerBrandsReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.serverBrandStyles.addAll(buffer.readList(new ArrayList<>(), () -> buffer.readBufferClass(new ServerBrandStyle())));
    }
}
