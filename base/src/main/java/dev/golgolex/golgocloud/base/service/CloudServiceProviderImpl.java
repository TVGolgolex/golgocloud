package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.service.CloudServiceProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudServiceProviderImpl implements CloudServiceProvider {

    private final List<CloudService> cloudServices = new ArrayList<>();

    @Override
    public void reloadServices() {
    }

    @Override
    public void updateService(@NotNull CloudService cloudService) {

    }

    @Override
    public void shutdownService(@NotNull CloudService cloudService) {

    }

    @Override
    public void prepareService(@NotNull CloudService cloudService) {

    }
}
