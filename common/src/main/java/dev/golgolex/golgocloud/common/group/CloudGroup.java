package dev.golgolex.golgocloud.common.group;

import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import dev.golgolex.quala.json.document.JsonDocument;
import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public final class CloudGroup implements BufferClass {

    private String name;
    private String splitter;
    private String javaCommand;

    private int minimalServiceCount;
    private int maximalServiceCount;
    private int startPort;

    private List<UUID> instances;
    private boolean staticService;
    private boolean maintenance;
    private ServiceEnvironment serviceEnvironment;

    private String displayItem;
    private String version;
    private long memory;

    private boolean onlineMode;
    private boolean fallback;
    private boolean joinByPort;
    private boolean tryStaticPort;

    private JsonDocument meta;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(name);
        codecBuffer.writeString(splitter);
        codecBuffer.writeString(javaCommand);

        codecBuffer.writeInt(minimalServiceCount);
        codecBuffer.writeInt(maximalServiceCount);
        codecBuffer.writeInt(startPort);

        codecBuffer.writeList(instances, CodecBuffer::writeUniqueId);
        codecBuffer.writeBoolean(staticService);
        codecBuffer.writeBoolean(maintenance);
        codecBuffer.writeEnum(serviceEnvironment);

        codecBuffer.writeString(displayItem);
        codecBuffer.writeString(version);
        codecBuffer.writeLong(memory);

        codecBuffer.writeBoolean(onlineMode);
        codecBuffer.writeBoolean(fallback);
        codecBuffer.writeBoolean(joinByPort);
        codecBuffer.writeBoolean(tryStaticPort);

        codecBuffer.writeDocument(meta);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.name = codecBuffer.readString();
        this.splitter = codecBuffer.readString();
        this.javaCommand = codecBuffer.readString();

        this.minimalServiceCount = codecBuffer.readInt();
        this.maximalServiceCount = codecBuffer.readInt();
        this.startPort = codecBuffer.readInt();

        this.instances = codecBuffer.readList(new ArrayList<>(), codecBuffer::readUniqueId);
        this.staticService = codecBuffer.readBoolean();
        this.maintenance = codecBuffer.readBoolean();
        this.serviceEnvironment = codecBuffer.readEnum(ServiceEnvironment.class);

        this.displayItem = codecBuffer.readString();
        this.version = codecBuffer.readString();
        this.memory = codecBuffer.readLong();

        this.onlineMode = codecBuffer.readBoolean();
        this.fallback = codecBuffer.readBoolean();
        this.joinByPort = codecBuffer.readBoolean();
        this.tryStaticPort = codecBuffer.readBoolean();

        this.meta = codecBuffer.readDocument();
    }
}