package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public final class CloudPermission implements BufferClass {

    @EqualsAndHashCode.Include
    private String permissionKey;
    @EqualsAndHashCode.Exclude
    private long createdTimestamp;
    @EqualsAndHashCode.Exclude
    private long removedTimestamp;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(this.permissionKey);
        codecBuffer.writeLong(this.createdTimestamp);
        codecBuffer.writeLong(this.removedTimestamp);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.permissionKey = codecBuffer.readString();
        this.createdTimestamp = codecBuffer.readLong();
        this.removedTimestamp = codecBuffer.readLong();
    }
}
