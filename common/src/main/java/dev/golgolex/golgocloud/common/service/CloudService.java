package dev.golgolex.golgocloud.common.service;

import dev.golgolex.golgocloud.common.service.environment.CloudProxyService;
import dev.golgolex.golgocloud.common.service.environment.CloudServerService;
import dev.golgolex.golgocloud.common.user.ServicePlayer;
import dev.golgolex.quala.netty5.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class CloudService implements BufferClass {

    private String id;
    private int serviceNumber;
    private UUID uuid;
    private String gameId;
    private ServiceEnvironment environment;

    private String group;
    @ApiStatus.Internal
    @Setter
    private UUID instance;
    private String template;
    @ApiStatus.Internal
    @Setter
    private String path;

    private String host;
    private int port;
    private boolean ready;
    private long memory;

    private int maxPlayers;
    private List<ServicePlayer> online;
    private List<ServicePlayer> connected;

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(id);
        codecBuffer.writeInt(serviceNumber);
        codecBuffer.writeUniqueId(uuid);
        codecBuffer.writeString(gameId);
        codecBuffer.writeEnum(environment);
        codecBuffer.writeString(group);
        codecBuffer.writeUniqueId(instance);
        codecBuffer.writeString(template);
        codecBuffer.writeString(path);
        codecBuffer.writeString(host);
        codecBuffer.writeInt(port);
        codecBuffer.writeBoolean(ready);
        codecBuffer.writeLong(memory);
        codecBuffer.writeInt(maxPlayers);
        codecBuffer.writeList(online, (it, servicePlayer) -> servicePlayer.writeBuffer(it));
        codecBuffer.writeList(connected, (it, servicePlayer) -> servicePlayer.writeBuffer(it));
        this.writeBufferService(codecBuffer);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        id = codecBuffer.readString();
        serviceNumber = codecBuffer.readInt();
        uuid = codecBuffer.readUniqueId();
        gameId = codecBuffer.readString();
        environment = codecBuffer.readEnum(ServiceEnvironment.class);
        group = codecBuffer.readString();
        instance = codecBuffer.readUniqueId();
        template = codecBuffer.readString();
        path = codecBuffer.readString();
        host = codecBuffer.readString();
        port = codecBuffer.readInt();
        ready = codecBuffer.readBoolean();
        memory = codecBuffer.readLong();
        maxPlayers = codecBuffer.readInt();
        online = codecBuffer.readList(new ArrayList<>(), () -> {
            ServicePlayer servicePlayer = new ServicePlayer();
            servicePlayer.readBuffer(codecBuffer);
            return servicePlayer;
        });
        connected = codecBuffer.readList(new ArrayList<>(), () -> {
            ServicePlayer servicePlayer = new ServicePlayer();
            servicePlayer.readBuffer(codecBuffer);
            return servicePlayer;
        });
        this.readBufferService(codecBuffer);
    }

    /**
     * Constructs a new instance of a CloudService based on the provided ServiceEnvironment.
     * @param serviceEnvironment the service environment (either SERVER or PROXY)
     * @param <T> the type of CloudService to construct
     * @return a new instance of the CloudService specified by the serviceEnvironment parameter
     */
    @SuppressWarnings("unchecked")
    public static <T extends CloudService> T constructEnrvionment(@NotNull ServiceEnvironment serviceEnvironment) {
        switch (serviceEnvironment) {
            case PROXY -> {
                return (T) new CloudProxyService();
            }
            case SERVER -> {
                return (T) new CloudServerService();
            }
        }
        return null;
    }

    /**
     * This method is an abstract method that should be implemented by subclasses of CloudService.
     * It is responsible for writing the buffer service data to a CodecBuffer.
     */
    public abstract void writeBufferService(@NotNull CodecBuffer codecBuffer);

    /**
     * This method represents the service for reading buffer data.
     * It is an abstract method, which means it must be implemented by any class that extends the CloudService class.
     * The implementation of this method will define how the buffer data is read and processed.
     * <p>
     * It does not take any parameters and does not return any values.
     * It is designed to be overridden by subclasses to provide the specific implementation for reading buffer data.
     */
    public abstract void readBufferService(@NotNull CodecBuffer codecBuffer);

    /**
     * Checks if the CloudService is full of players.
     *
     * @return true if the number of online players is equal to or greater than the maximum number of players, false otherwise.
     */
    public boolean isFull() {
        if (this.maxPlayers == -1) return false;
        return this.online.size() >= this.maxPlayers;
    }

    /**
     * Calculates the percentage of online players out of the maximum allowed players.
     *
     * @return The percentage of online players out of the maximum allowed players.
     */
    public double onlineCountPercent() {
        var onlinePlayersCount = this.online.size();
        var maximalPlayers = this.maxPlayers;
        if (maximalPlayers == 0) {
            return 0.0;
        } else {
            return ((double) onlinePlayersCount / maximalPlayers) * 100.0;
        }
    }
}
