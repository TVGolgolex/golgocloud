package dev.golgolex.golgocloud.common.user;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The CloudPlayerProvider interface represents a provider of CloudPlayer objects.
 * Implementations of this interface provide methods to retrieve CloudPlayer objects
 * based on unique identifiers or names.
 */
public interface CloudPlayerProvider {

    List<CloudPlayer> cloudPlayers();

    void reloadPlayers();

    /**
     * Retrieves a CloudPlayer object based on the given unique identifier.
     *
     * @param uniqueId The unique identifier of the CloudPlayer.
     * @return The CloudPlayer object matching the unique identifier.
     */
    CloudPlayer cloudPlayer(@NotNull UUID uniqueId);

    /**
     * Retrieves a CloudPlayer based on the provided name.
     * @param name The name of the CloudPlayer.
     * @return The CloudPlayer object with the provided name, or null if not found.
     */
    CloudPlayer cloudPlayer(@NotNull String name);

    /**
     * Retrieves an optional CloudPlayer object based on the provided unique identifier.
     *
     * @param uniqueId the unique identifier of the CloudPlayer
     * @return an optional CloudPlayer object if found, otherwise an empty optional
     * @throws NullPointerException if the uniqueId is null
     */
    default Optional<CloudPlayer> optionalCloudPlayer(@NotNull UUID uniqueId) {
        return Optional.ofNullable(this.cloudPlayer(uniqueId));
    }

    /**
     * Returns an optional CloudPlayer object based on the given name.
     *
     * @param name the name of the CloudPlayer
     * @return an optional CloudPlayer object if it exists, otherwise an empty optional
     */
    default Optional<CloudPlayer> optionalCloudPlayer(@NotNull String name) {
        return Optional.ofNullable(this.cloudPlayer(name));
    }

    /**
     * Returns a list of online CloudPlayer objects.
     *
     * @return A list of online CloudPlayer objects.
     */
    default List<CloudPlayer> onlineCloudPlayers() {
        return this.cloudPlayers().stream().filter(CloudPlayer::isOnline).toList();
    }

    /**
     * Returns a list of CloudPlayer objects that are currently offline.
     *
     * @return A list of offline CloudPlayer objects.
     */
    default List<CloudPlayer> offlineCloudPlayers() {
        return this.cloudPlayers().stream().filter(cloudPlayer -> !cloudPlayer.isOnline()).toList();
    }

    /**
     * Creates a CloudPlayer object and adds it to the cloud player provider.
     *
     * @param cloudPlayer The CloudPlayer object to be created and added.
     */
    void createCloudPlayer(@NotNull CloudPlayer cloudPlayer);

    /**
     * Updates the CloudPlayer information in the cloud player provider.
     *
     * @param cloudPlayer The CloudPlayer object to be updated.
     */
    void updateCloudPlayer(@NotNull CloudPlayer cloudPlayer);
}
