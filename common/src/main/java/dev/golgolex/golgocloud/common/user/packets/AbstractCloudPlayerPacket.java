package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.basic.protocol.Packet;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public class AbstractCloudPlayerPacket extends Packet {

    private final CloudPlayer cloudPlayer;

    public AbstractCloudPlayerPacket(@NotNull CloudPlayer cloudPlayer) {
        this.cloudPlayer = cloudPlayer;
        this.cloudPlayer.writeBuffer(buffer);
    }

    public AbstractCloudPlayerPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudPlayer = new CloudPlayer();
        this.cloudPlayer.readBuffer(buffer);
    }
}
