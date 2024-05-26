package dev.golgolex.golgocloud.common.group.packets;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

@Getter
@Accessors(fluent = true)
public abstract class AbstractCloudGroupPacket extends Packet {

    private final CloudGroup cloudGroup;

    /**
     * Creates an instance of AbstractCloudGroupPacket.
     *
     * @param cloudGroup the CloudGroup object to be assigned to {@code this.cloudGroup}
     *
     * @throws NullPointerException if {@code cloudGroup} is null
     */
    public AbstractCloudGroupPacket(@NotNull CloudGroup cloudGroup) {
        this.cloudGroup = cloudGroup;
        this.cloudGroup.writeBuffer(buffer);
    }

    /**
     * Creates a new instance of {@code AbstractCloudGroupPacket} with the given {@code CodecBuffer} buffer.
     *
     * @param buffer the buffer used for reading data
     */
    public AbstractCloudGroupPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudGroup = new CloudGroup();
        this.cloudGroup.readBuffer(buffer);
    }
}
