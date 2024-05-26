package dev.golgolex.golgocloud.common.instance;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * The CloudInstanceProvider interface represents a provider for retrieving and managing cloud instances.
 */
public interface CloudInstanceProvider {

    /**
     * Retrieves the list of cloud instances.
     *
     * @return The list of cloud instances.
     */
    List<CloudInstance> cloudInstances();

    /**
     * Retrieves the CloudInstance with the specified ID.
     *
     * @param id The ID of the CloudInstance.
     * @return The CloudInstance with the specified ID, or null if not found.
     */
    default CloudInstance cloudInstance(@NotNull String id) {
        return this.cloudInstances().stream().filter(it -> it.id().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    /**
     * Retrieves the CloudInstance with the specified UUID.
     *
     * @param uuid The UUID of the CloudInstance to retrieve. Cannot be null.
     * @return The CloudInstance object with the specified UUID, or null if not found.
     */
    default CloudInstance cloudInstance(@NotNull UUID uuid) {
        return this.cloudInstances().stream().filter(it -> it.uuid().equals(uuid)).findFirst().orElse(null);
    }

    /**
     * Reloads the list of cloud instances.
     * <p>
     * This method is used to refresh the list of cloud instances obtained from the {@link CloudInstanceProvider#cloudInstances()} method.
     * It can be called when there is a need to update the information or fetch the latest data about the cloud instances.
     * </p>
     */
    void reloadInstances();

    /**
     * Updates the specified CloudInstance object.
     *
     * @param cloudInstance the CloudInstance object to be updated
     */
    void updateInstance(@NotNull CloudInstance cloudInstance);

    /**
     * Shuts down the specified cloud instance.
     *
     * @param cloudInstance the cloud instance to be shut down (must not be null)
     */
    void shutdownInstance(@NotNull CloudInstance cloudInstance);

}
