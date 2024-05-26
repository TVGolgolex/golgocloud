package dev.golgolex.golgocloud.cloudapi.group;

import dev.golgolex.golgocloud.common.group.CloudGroup;
import dev.golgolex.golgocloud.common.group.CloudGroupProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudGroupProviderImpl implements CloudGroupProvider {

    private final List<CloudGroup> cloudGroups = new ArrayList<>();

    @Override
    public void reloadGroups() {

    }

    @Override
    public void updateGroup(@NotNull CloudGroup cloudGroup) {

    }

    @Override
    public void deleteGroup(@NotNull CloudGroup cloudGroup) {

    }

    @Override
    public void createGroup(@NotNull CloudGroup cloudGroup) {

    }
}
