package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.common.json.JsonDocument;
import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@EqualsAndHashCode(callSuper = false)
public class CloudPermissibleEntity extends CloudPermissible {

    private UUID uniqueId;
    private List<GroupEntry> groupEntries;

    public CloudPermissibleEntity(JsonDocument properties,
                                  List<CloudPermission> activePermissions,
                                  UUID uniqueId,
                                  List<GroupEntry> groupEntries) {
        super(properties, activePermissions);
        this.uniqueId = uniqueId;
        this.groupEntries = groupEntries;
    }

    public CloudPermissibleEntity() {
    }

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        super.writeBuffer(codecBuffer);
        codecBuffer.writeUniqueId(this.uniqueId);
        codecBuffer.writeList(this.groupEntries, CodecBuffer::writeBufferClass);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        super.readBuffer(codecBuffer);
        this.uniqueId = codecBuffer.readUniqueId();
        this.groupEntries = codecBuffer.readList(new ArrayList<>(), () -> codecBuffer.readBufferClass(new GroupEntry()));
    }

    public PermissionCheckResult hasPermission(@NotNull CloudPermissionPool permissionPool, @NotNull String permission) {
        if (this.permissionCheckResult("*").asBoolean() || this.permissionCheckResult(permission).asBoolean()) {
            return PermissionCheckResult.ALLOWED;
        }
        for (var permissibleGroup : permissionPool.permissibleGroups()) {
            if (this.groupEntries.stream().noneMatch(groupEntry -> groupEntry.name().equalsIgnoreCase(permissibleGroup.name()))) {
                continue;
            }
            if (permissibleGroup.permissionCheckResult("*").asBoolean() || permissibleGroup.permissionCheckResult(permission).asBoolean()) {
                return PermissionCheckResult.ALLOWED;
            }
        }
        return PermissionCheckResult.DENY;
    }

    @Getter
    @Accessors(fluent = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    public static class GroupEntry implements BufferClass {

        private String name;
        @EqualsAndHashCode.Exclude
        private long untilTimestamp;
        @EqualsAndHashCode.Exclude
        private long removedTimestamp;
        @EqualsAndHashCode.Exclude
        private int priority;

        @Override
        public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
            codecBuffer.writeString(this.name);
            codecBuffer.writeLong(this.untilTimestamp);
            codecBuffer.writeLong(this.removedTimestamp);
            codecBuffer.writeInt(this.priority);
        }

        @Override
        public void readBuffer(@NotNull CodecBuffer codecBuffer) {
            this.name = codecBuffer.readString();
            this.untilTimestamp = codecBuffer.readLong();
            this.removedTimestamp = codecBuffer.readLong();
            this.priority = codecBuffer.readInt();
        }
    }
}
