package dev.golgolex.golgocloud.plugin.paper.permission;

import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.golgocloud.common.permission.PermissionCheckResult;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class PaperPermissionInjector extends PermissibleBase {

    private final Player player;
    private final CloudPermissionPool permissionPool;

    public PaperPermissionInjector(@NotNull Player player, @NotNull CloudPermissionPool permissionPool) {
        super(player);
        this.player = player;
        this.permissionPool = permissionPool;
    }

    private @NotNull Set<Permission> defaultPermissions() {
        return this.player.getServer().getPluginManager().getDefaultPermissions(false);
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return super.hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return super.hasPermission(perm.getName());
    }

    @Override
    public boolean hasPermission(@NotNull Permission perm) {
        return super.hasPermission(perm.getName());
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        try {
            var cloudPermissibleEntity = this.permissionPool.permissibleEntity(this.player.getUniqueId());
            System.out.println(1);
            if (cloudPermissibleEntity == null) {
                System.out.println(2);
                return false;
            }
            System.out.println(3);

            for (var permissibleGroup : permissionPool.permissibleGroups()) {
                System.out.println("Group: " + permissibleGroup.name());
                for (var activePermission : permissibleGroup.activePermissions()) {
                    System.out.println("Group: " + permissibleGroup.name() + " - " + activePermission.permissionKey());
                }
            }

            System.out.println(cloudPermissibleEntity.hasPermission(permissionPool, "*").name());

            for (var defaultPermission : this.defaultPermissions()) {
                System.out.println(4);
                if (defaultPermission.getName().equalsIgnoreCase(inName)) {
                    System.out.println(inName);
                    System.out.println(5);
                    var result = cloudPermissibleEntity.hasPermission(this.permissionPool, inName);
                    System.out.println(6);
                    return result == PermissionCheckResult.DENY || result.asBoolean();
                }
                System.out.println(8);
            }

            System.out.println(8);
            var result = cloudPermissibleEntity.hasPermission(this.permissionPool, inName);
            System.out.println(9);
            if (result != PermissionCheckResult.DENY) {
                System.out.println(10);
                return result.asBoolean();
            }

            System.out.println(11);
            return this.testParents(inName, per -> cloudPermissibleEntity.hasPermission(this.permissionPool, inName));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public synchronized @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        var attachmentInfos = new HashSet<PermissionAttachmentInfo>();
        for (var cloudPermission : this.permissionPool.allPermissions(this.permissionPool.permissibleEntity(this.player.getUniqueId()))) {
            var bukkitPermission = this.player.getServer().getPluginManager().getPermission(cloudPermission.permissionKey());
            if (bukkitPermission != null) {
                this.forEachChildren(
                        bukkitPermission,
                        (name, value) -> attachmentInfos.add(new PermissionAttachmentInfo(this, cloudPermission.permissionKey(), null, true))
                );
            } else {
                attachmentInfos.add(new PermissionAttachmentInfo(this, cloudPermission.permissionKey(), null, true));
            }
        }
        for (final var defaultPermission : this.defaultPermissions()) {
            this.forEachChildren(defaultPermission, (name, value) -> attachmentInfos.add(new PermissionAttachmentInfo(this, name, null, value)));
        }
        return attachmentInfos;
    }

    private boolean testParents(@NotNull String inName, @NotNull Function<Permission, PermissionCheckResult> parentAcceptor) {
        for (var parent : this.player.getServer().getPluginManager().getPermissions()) {
            var result = this.testParents(inName, parent, null, parentAcceptor);
            if (result != PermissionCheckResult.DENY) {
                return result.asBoolean();
            }
        }
        return false;
    }

    private PermissionCheckResult testParents(@NotNull String inName, @NotNull Permission parent, @Nullable Permission lastParent, @NotNull Function<Permission, PermissionCheckResult> parentAcceptor) {
        for (var entry : parent.getChildren().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(inName)) {
                var result = parentAcceptor.apply(Objects.requireNonNullElse(lastParent, parent));
                if (result != PermissionCheckResult.DENY) {
                    return PermissionCheckResult.fromBoolean(entry.getValue());
                }
                continue;
            }

            var child = this.player.getServer().getPluginManager().getPermission(entry.getKey());
            if (child != null) {
                var result = this.testParents(inName, child, parent, parentAcceptor);
                if (result != PermissionCheckResult.DENY) {
                    return result;
                }
            }
        }

        return PermissionCheckResult.DENY;
    }

    private void forEachChildren(@NotNull Permission permission, @NotNull BiConsumer<String, Boolean> permissionAcceptor) {
        permissionAcceptor.accept(permission.getName(), true);
        for (var entry : permission.getChildren().entrySet()) {
            permissionAcceptor.accept(entry.getKey(), entry.getValue());
        }
    }
}