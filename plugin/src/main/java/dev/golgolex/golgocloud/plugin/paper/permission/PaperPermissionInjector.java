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

    public PaperPermissionInjector(
            @NotNull Player player,
            @NotNull CloudPermissionPool permissionPool) {
        super(player);
        this.player = player;
        this.permissionPool = permissionPool;
    }

    private @NotNull Set<Permission> defaultPermissions() {
        return this.player.getServer().getPluginManager().getDefaultPermissions(false);
    }

    @Override
    public synchronized @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
        var attachmentInfos = new HashSet<PermissionAttachmentInfo>();
        for (var permission : this.permissionPool.allPermissions(this.permissionPool.permissibleEntity(player.getUniqueId()))) {
            var bukkit = this.player.getServer().getPluginManager().getPermission(permission.permissionKey());
            if (bukkit != null) {
                this.forEachChildren(
                        bukkit,
                        (name, value) -> attachmentInfos.add(new PermissionAttachmentInfo(this, name, null, value))
                );
            } else {
                attachmentInfos.add(new PermissionAttachmentInfo(this, permission.permissionKey(), null, true));
            }
        }
        for (final var defaultPermission : this.defaultPermissions()) {
            this.forEachChildren(defaultPermission, (name, value) -> attachmentInfos.add(new PermissionAttachmentInfo(this, name, null, value)));
        }
        return attachmentInfos;
    }

    @Override
    public boolean isPermissionSet(@NotNull String name) {
        return this.hasPermission(name);
    }

    @Override
    public boolean isPermissionSet(@NotNull Permission perm) {
        return this.hasPermission(perm.getName());
    }

    @Override
    public boolean hasPermission(@NotNull String inName) {
        try {
            var user = this.permissionPool.permissibleEntity(this.player.getUniqueId());
            if (user == null) {
                System.out.println("permission entity is null");
                return false;
            }

            for (var defaultPermission : this.defaultPermissions()) {
                if (defaultPermission.getName().equalsIgnoreCase(inName)) {
                    var result = user.hasPermission(permissionPool, inName);
                    return result == PermissionCheckResult.DENY || result.asBoolean();
                }
            }

            var result = user.hasPermission(permissionPool, inName);
            if (result != PermissionCheckResult.DENY) {
                return result.asBoolean();
            }

            return this.testParents(inName, perm -> user.hasPermission(permissionPool, perm.getName()));
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return false;
        }
    }

    private boolean testParents(
            @NotNull String inName,
            @NotNull Function<Permission, PermissionCheckResult> parentAcceptor
    ) {
        for (var parent : this.player.getServer().getPluginManager().getPermissions()) {
            var result = this.testParents(inName, parent, null, parentAcceptor);
            if (result != PermissionCheckResult.DENY) {
                return result.asBoolean();
            }
        }
        return false;
    }

    private PermissionCheckResult testParents(
            @NotNull String inName,
            @NotNull Permission parent,
            @Nullable Permission lastParent,
            @NotNull Function<Permission, PermissionCheckResult> parentAcceptor
    ) {
        for (var entry : parent.getChildren().entrySet()) {
            if (entry.getKey().equalsIgnoreCase(inName)) {
                PermissionCheckResult result;
                result = parentAcceptor.apply(Objects.requireNonNullElse(lastParent, parent));

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

    private void forEachChildren(
            @NotNull Permission permission,
            @NotNull BiConsumer<String, Boolean> permissionAcceptor
    ) {
        permissionAcceptor.accept(permission.getName(), true);
        for (var entry : permission.getChildren().entrySet()) {
            permissionAcceptor.accept(entry.getKey(), entry.getValue());
        }
    }
}