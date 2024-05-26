package dev.golgolex.golgocloud.common.service;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The CloudServiceProvider interface represents a provider of cloud services.
 * It provides methods to interact with the cloud services and perform various operations.
 */
public interface CloudServiceProvider {

    List<CloudService> cloudServices();

    /**
     * Calculates the total number of players across all cloud services.
     *
     * @return The total number of players.
     */
    default int totalPlayers() {
        var total = 0;
        for (var service : cloudServices()) {
            total += service.maxPlayers();
        }
        return total;
    }

    /**
     * Calculates the average online count percentage across all cloud services.
     *
     * @return The average online count percentage.
     */
    default double averageOnlineCountPercent() {
        double totalPercent = 0;
        var count = 0;
        for (var service : cloudServices()) {
            totalPercent += service.onlineCountPercent();
            count++;
        }
        return totalPercent / count;
    }

    /**
     * Retrieves a cloud service by its ID.
     *
     * @param id The ID of the cloud service to retrieve.
     * @return An Optional containing the cloud service if found, otherwise an empty Optional.
     */
    default Optional<CloudService> cloudService(@NotNull String id) {
        return this.cloudServices().stream().filter(cloudService -> cloudService.id().equalsIgnoreCase(id)).findFirst();
    }

    /**
     * Retrieves a list of cloud services based on the specified group.
     *
     * @param group the group to filter the cloud services by (not null)
     * @return a list of cloud services that belong to the specified group
     * @throws NullPointerException if the group parameter is null
     */
    default List<CloudService> cloudServices(@NotNull String group) {
        return this.cloudServices().stream().filter(cloudService -> cloudService.group().equalsIgnoreCase(group)).collect(Collectors.toList());
    }

    /**
     * Reloads the services in the cloud provider.
     * This method should be called whenever there are changes or updates to the services.
     */
    void reloadServices();

    /**
     * Updates the given CloudService.
     *
     * @param cloudService the CloudService to update
     */
    void updateService(@NotNull CloudService cloudService);

    /**
     * Shuts down the specified cloud service.
     *
     * @param cloudService the cloud service to be shutdown
     * @throws NullPointerException if the cloudService parameter is null
     */
    void shutdownService(@NotNull CloudService cloudService);

    /**
     * Prepares the given CloudService for use.
     *
     * @param cloudService the CloudService to prepare
     */
    void prepareService(@NotNull CloudService cloudService);

}
