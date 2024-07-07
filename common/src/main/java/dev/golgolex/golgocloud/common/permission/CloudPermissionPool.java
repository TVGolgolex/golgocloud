package dev.golgolex.golgocloud.common.permission;

import dev.golgolex.quala.netty5.basic.protocol.buffer.BufferClass;
import dev.golgolex.quala.netty5.basic.protocol.buffer.CodecBuffer;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CloudPermissionPool implements BufferClass {

    private String id;
    private UUID uuid;
    @Setter(AccessLevel.PROTECTED)
    private List<CloudPermissibleGroup> permissibleGroups = new ArrayList<>();
    @Setter(AccessLevel.PROTECTED)
    private List<CloudPermissibleEntity> permissibleEntities = new ArrayList<>();

    public CloudPermissibleGroup permissibleGroup(@NotNull String name) {
        return this.permissibleGroups.stream().filter(permissibleGroup -> permissibleGroup.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public CloudPermissibleEntity permissibleEntity(@NotNull UUID uniqueId) {
        return this.permissibleEntities.stream().filter(permissibleEntity -> permissibleEntity.uniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public void updatePermissible(@NotNull CloudPermissibleEntity permissibleEntity) {
        this.permissibleEntities.removeIf(cloudPermissibleEntities -> cloudPermissibleEntities.equals(permissibleEntity));
        this.permissibleEntities.add(permissibleEntity);
    }

    public void updatePermissible(@NotNull CloudPermissibleGroup permissibleGroup) {
        this.permissibleGroups.removeIf(permissibleGroups -> permissibleGroups.equals(permissibleGroup));
        this.permissibleGroups.add(permissibleGroup);
    }

    public void createPermissible(@NotNull CloudPermissibleGroup permissibleGroup) {
        if (this.permissibleGroups.stream().anyMatch(permissibleGroups -> permissibleGroups.equals(permissibleGroup))) {
            return;
        }
        this.permissibleGroups.add(permissibleGroup);
    }

    public void createPermissible(@NotNull CloudPermissibleEntity permissibleEntity) {
        if (this.permissibleEntities.stream().anyMatch(permissibleEntities -> permissibleEntities.equals(permissibleEntity))) {
            return;
        }
        this.permissibleEntities.add(permissibleEntity);
    }

    public void deletePermissible(@NotNull CloudPermissibleGroup permissibleGroup) {
        if (this.permissibleGroups.stream().noneMatch(permissibleGroups -> permissibleGroups.equals(permissibleGroup))) {
            return;
        }
        this.permissibleGroups.removeIf(permissibleGroups -> permissibleGroups.equals(permissibleGroup));
    }

    public void deletePermissible(@NotNull CloudPermissibleEntity permissibleEntity) {
        if (this.permissibleEntities.stream().noneMatch(permissibleEntities -> permissibleEntities.equals(permissibleEntity))) {
            return;
        }
        this.permissibleEntities.removeIf(permissibleEntities -> permissibleEntities.equals(permissibleEntity));
    }

    public List<CloudPermission> allPermissions(@NotNull CloudPermissible permissible) {
        if (permissible instanceof CloudPermissibleGroup permissibleGroup) {
            return permissibleGroup.activePermissions();
        }
        if (permissible instanceof CloudPermissibleEntity permissibleEntity) {
            var permissions = new ArrayList<>(permissibleEntity.activePermissions());
            for (var groupEntry : permissibleEntity.groupEntries()) {
                var group = this.permissibleGroup(groupEntry.name());
                if (group == null) {
                    continue;
                }
                permissions.addAll(group.activePermissions());
            }
            return permissions;
        }
        return new ArrayList<>();
    }

    @Override
    public void writeBuffer(@NotNull CodecBuffer codecBuffer) {
        codecBuffer.writeString(this.id);
        codecBuffer.writeUniqueId(this.uuid);
        codecBuffer.writeList(this.permissibleGroups, CodecBuffer::writeBufferClass);
        codecBuffer.writeList(this.permissibleEntities, CodecBuffer::writeBufferClass);
    }

    @Override
    public void readBuffer(@NotNull CodecBuffer codecBuffer) {
        this.id = codecBuffer.readString();
        this.uuid = codecBuffer.readUniqueId();
        this.permissibleGroups = codecBuffer.readList(new ArrayList<>(), () -> codecBuffer.readBufferClass(new CloudPermissibleGroup()));
        this.permissibleEntities = codecBuffer.readList(new ArrayList<>(), () -> codecBuffer.readBufferClass(new CloudPermissibleEntity()));
    }
}
