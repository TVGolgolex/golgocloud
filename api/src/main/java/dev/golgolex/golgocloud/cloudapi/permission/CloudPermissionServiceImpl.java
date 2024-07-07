package dev.golgolex.golgocloud.cloudapi.permission;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleEntity;
import dev.golgolex.golgocloud.common.permission.CloudPermissibleGroup;
import dev.golgolex.golgocloud.common.permission.CloudPermissionPool;
import dev.golgolex.golgocloud.common.permission.CloudPermissionService;
import dev.golgolex.golgocloud.common.permission.packets.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudPermissionServiceImpl implements CloudPermissionService {

    private final List<CloudPermissionPool> permissionPools = new ArrayList<>();

    @Override
    public void reload() {
        CloudPermissionPoolsReloadReplyPacket replyPacket = CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendQuery(new CloudPermissionPoolsReloadPacket());

        this.permissionPools.clear();
        this.permissionPools.addAll(replyPacket.permissionPools());
    }

    @Override
    public void reload(@NotNull CloudPermissionPool permissionPool) {
        CloudPermissionPoolReloadReplyPacket replyPacket = CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendQuery(new CloudPermissionPoolReloadPacket(permissionPool.id(), permissionPool.uuid()));

        this.permissionPools.removeIf(permissionPools -> permissionPools.equals(replyPacket.permissionPool()));
        this.permissionPools.add(replyPacket.permissionPool());
    }

    @Override
    public void createPermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendPacket(new CloudPermissionPoolCreatePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissionPool));
    }

    @Override
    public void deletePermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendPacket(new CloudPermissionPoolDeletePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissionPool));
    }

    @Override
    public void updatePermissionPool(@NotNull CloudPermissionPool permissionPool) {
        CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendPacket(new CloudPermissionPoolUpdatePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissionPool));
    }

    @Override
    public void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleGroup permissibleGroup) {
        CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendPacket(new CloudPermissionPoolUpdatePermissiblePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissibleGroup));
    }

    @Override
    public void updatePermissible(@NotNull CloudPermissionPool permissionPool, @NotNull CloudPermissibleEntity permissibleEntity) {
        CloudAPI.instance()
                .nettyClient()
                .thisNetworkChannel()
                .sendPacket(new CloudPermissionPoolUpdatePermissiblePacket(
                        permissionPool.id(),
                        permissionPool.uuid(),
                        permissibleEntity));
    }
}
