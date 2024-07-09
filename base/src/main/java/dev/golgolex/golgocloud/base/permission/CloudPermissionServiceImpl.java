package dev.golgolex.golgocloud.base.permission;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.base.configuration.PermissionsConfiguration;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleEntity;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.golgocloud.common.permission.CloudPermissionService;
import dev.golgolex.golgocloud.common.permission.packets.CloudPermissionPoolUpdatePermissiblePacket;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionServiceImpl implements CloudPermissionService {

    private final List<CloudPermissionPool> permissionPools = new ArrayList<>();

    @Override
    public void reload() {
        this.permissionPools.clear();
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;
            this.permissionPools.addAll(configuration.permissionPools());
            CloudBase.instance().logger().info("Loaded following permission pools: &3" + this.permissionPools.stream()
                    .map(CloudPermissionPool::id)
                    .collect(Collectors.joining("&2, &3")));

            for (var permissionPool : this.permissionPools) {
                for (CloudPermissibleGroup permissibleGroup : permissionPool.permissibleGroups()) {
                    System.out.println("Group: " + permissibleGroup.name());
                    for (var activePermission : permissibleGroup.activePermissions()) {
                        System.out.println("Group: " + permissibleGroup.name() + " - " + activePermission.permissionKey());
                    }
                }
            }

        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
    }

    @Override
    public void reload(@NotNull CloudPermissionPool permissionPool) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;
            configuration.permissionPools()
                    .stream()
                    .filter(permissionPools -> permissionPools.id().equalsIgnoreCase(permissionPool.id()))
                    .findFirst()
                    .ifPresent(this.permissionPools::add);
            this.permissionPools.removeIf(permissionPools -> permissionPools.id().equalsIgnoreCase(permissionPool.id()));
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
    }

    @Override
    public void createPermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;

            var x = new ArrayList<>(configuration.permissionPools());
            x.removeIf(permissionPools -> permissionPools.id().equalsIgnoreCase(permissionPool.id()));
            x.add(permissionPool);

            configuration.permissionPools(x);
            configuration.save();
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
        this.reload();
    }

    @Override
    public void deletePermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;

            var x = new ArrayList<>(configuration.permissionPools());
            x.removeIf(permissionPools -> permissionPools.id().equalsIgnoreCase(permissionPool.id()));

            configuration.permissionPools(x);
            configuration.save();
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
        this.reload();
    }

    @Override
    public void updatePermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;

            var x = new ArrayList<>(configuration.permissionPools());
            x.removeIf(permissionPools -> permissionPools.id().equalsIgnoreCase(permissionPool.id()));
            x.add(permissionPool);

            configuration.permissionPools(x);
            configuration.save();
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
        this.reload();
    }

    @Override
    public void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleGroup permissibleGroup) {
        CloudBase.instance()
                .nettyServer()
                .serverChannelTransmitter()
                .sendPacketToAll(new CloudPermissionPoolUpdatePermissiblePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissibleGroup), null);
    }

    public void handleUpdatePermissible(@NotNull String permissionPoolId, @NotNull CloudPermissibleGroup permissibleGroup) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;
            var permissionPool = configuration.permissionPools().stream().filter(it -> it.id().equalsIgnoreCase(permissionPoolId)).findFirst().orElse(null);

            if (permissionPool == null)
                return;

            permissionPool.updatePermissible(permissibleGroup);
            var cloudPermissionPools = new ArrayList<>(configuration.permissionPools());
            cloudPermissionPools.removeIf(permissionPools -> permissionPools.equals(permissionPool));
            cloudPermissionPools.add(permissionPool);
            configuration.permissionPools(cloudPermissionPools);
            configuration.save();
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
    }

    public void handleUpdatePermissible(@NotNull String permissionPoolId, @NotNull CloudPermissibleEntity permissibleEntity) {
        CloudBase.instance().configurationService().configurationOptional("permissions").ifPresentOrElse(configurationClass -> {
            var configuration = (PermissionsConfiguration) configurationClass;
            var permissionPool = configuration.permissionPools().stream().filter(it -> it.id().equalsIgnoreCase(permissionPoolId)).findFirst().orElse(null);

            if (permissionPool == null)
                return;

            permissionPool.updatePermissible(permissibleEntity);
            var cloudPermissionPools = new ArrayList<>(configuration.permissionPools());
            cloudPermissionPools.removeIf(permissionPools -> permissionPools.equals(permissionPool));
            cloudPermissionPools.add(permissionPool);
            configuration.permissionPools(cloudPermissionPools);
            configuration.save();
        }, () -> CloudBase.instance().logger().error("No permissions configuration found.", null));
    }

    @Override
    public void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleEntity permissibleEntity) {
        CloudBase.instance()
                .nettyServer()
                .serverChannelTransmitter()
                .sendPacketToAll(new CloudPermissionPoolUpdatePermissiblePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissibleEntity), null);
    }
}
