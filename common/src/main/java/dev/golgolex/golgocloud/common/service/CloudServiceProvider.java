package dev.golgolex.golgocloud.common.service;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CloudServiceProvider {

    List<CloudService> cloudServices();

    default int totalPlayers() {
        var total = 0;
        for (var service : cloudServices()) {
            total += service.maxPlayers();
        }
        return total;
    }

    default double averageOnlineCountPercent() {
        double totalPercent = 0;
        var count = 0;
        for (var service : cloudServices()) {
            totalPercent += service.onlineCountPercent();
            count++;
        }
        return totalPercent / count;
    }

    default Optional<CloudService> cloudService(@NotNull String id) {
        return this.cloudServices().stream().filter(cloudService -> cloudService.id().equalsIgnoreCase(id)).findFirst();
    }

    default List<CloudService> cloudServices(@NotNull String group) {
        return this.cloudServices().stream().filter(cloudService -> cloudService.group().equalsIgnoreCase(group)).collect(Collectors.toList());
    }

    void reloadServices();

    void updateService(@NotNull CloudService cloudService);

    void shutdownService(@NotNull CloudService cloudService);

    void prepareService(@NotNull CloudService cloudService);

}
