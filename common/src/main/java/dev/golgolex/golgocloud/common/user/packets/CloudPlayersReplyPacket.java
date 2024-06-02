package dev.golgolex.golgocloud.common.user.packets;

import dev.golgolex.golgocloud.common.user.CloudPlayer;
import dev.golgolex.quala.netty5.protocol.Packet;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudPlayersReplyPacket extends Packet {

    private final List<CloudPlayer> cloudPlayers;

    public CloudPlayersReplyPacket(List<CloudPlayer> cloudPlayers) {
        this.cloudPlayers = cloudPlayers;
        buffer.writeList(this.cloudPlayers, (codecBuffer, cloudPlayer) -> cloudPlayer.writeBuffer(codecBuffer));
    }

    public CloudPlayersReplyPacket(CodecBuffer buffer) {
        super(buffer);
        this.cloudPlayers = buffer.readList(new ArrayList<>(), () -> {
            var cloudPlayer = new CloudPlayer();
            cloudPlayer.readBuffer(buffer);
            return cloudPlayer;
        });
    }
}
