package dev.golgolex.golgocloud.common.user;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class CloudPlayer implements BufferClass {

    // primary
    private UUID uniqueId;
    private String username;

    // useful
    private JsonDocument meta;
    private Timestamp firstLogin;
    private Timestamp lastLogin;
    private Timestamp connectionTime;
    private long onlineTime;
    private List<String> hosts;
    private List<String> names;

    private String language;
    private String branding;

    @ApiStatus.Internal
    @Setter
    private OnlineCredentials onlineCredentials;
    @ApiStatus.Internal
    @Setter
    private boolean waitingForTransfer;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeUniqueId(this.uniqueId);
        codecBuffer.writeString(this.username);
        codecBuffer.writeDocument(this.meta);
        codecBuffer.writeLong(this.firstLogin.getTime());
        codecBuffer.writeLong(this.lastLogin.getTime());
        codecBuffer.writeLong(this.connectionTime.getTime());
        codecBuffer.writeLong(this.onlineTime);
        codecBuffer.writeList(this.hosts, CodecBuffer::writeString);
        codecBuffer.writeList(this.names, CodecBuffer::writeString);
        codecBuffer.writeString(this.language);
        codecBuffer.writeString(this.branding);
        codecBuffer.writeNullable(this.onlineCredentials, it -> this.onlineCredentials.writeBuffer(it));
        codecBuffer.writeBoolean(this.waitingForTransfer);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.uniqueId = codecBuffer.readUniqueId();
        this.username = codecBuffer.readString();
        this.meta = codecBuffer.readDocument();
        this.firstLogin = new Timestamp(codecBuffer.readLong());
        this.lastLogin = new Timestamp(codecBuffer.readLong());
        this.connectionTime = new Timestamp(codecBuffer.readLong());
        this.onlineTime = codecBuffer.readLong();
        this.hosts = codecBuffer.readList(new ArrayList<>(), codecBuffer::readString);
        this.names = codecBuffer.readList(new ArrayList<>(), codecBuffer::readString);
        this.language = codecBuffer.readString();
        this.branding = codecBuffer.readString();
        this.onlineCredentials = codecBuffer.readNullable(OnlineCredentials.class, () -> {
            var credentials = new OnlineCredentials();
            credentials.readBuffer(codecBuffer);
            return credentials;
        });
        this.waitingForTransfer = codecBuffer.readBoolean();
    }

    public boolean isOnline() {
        return this.onlineCredentials != null;
    }
}