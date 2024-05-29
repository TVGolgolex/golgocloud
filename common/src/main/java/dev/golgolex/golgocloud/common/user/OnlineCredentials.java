package dev.golgolex.golgocloud.common.user;

import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class OnlineCredentials implements BufferClass {

    private String ip;
    private String currentServer;
    private String lastServer;
    private List<String> connectedServers;
    private String currentProxy;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(this.ip);
        codecBuffer.writeString(this.currentServer);
        codecBuffer.writeString(this.lastServer);
        codecBuffer.writeStringList(this.connectedServers);
        codecBuffer.writeBoolean(this.currentProxy != null);
        if (this.currentProxy != null) {
            codecBuffer.writeString(this.currentProxy);
        }
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.ip = codecBuffer.readString();
        this.currentServer = codecBuffer.readString();
        this.lastServer = codecBuffer.readString();
        this.connectedServers = codecBuffer.readStringList(new ArrayList<>());
        this.currentProxy = codecBuffer.readBoolean() ? codecBuffer.readString() : null;
    }
}