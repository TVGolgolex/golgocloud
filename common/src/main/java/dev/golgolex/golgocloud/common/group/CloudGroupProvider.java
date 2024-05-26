package dev.golgolex.golgocloud.common.group;

import dev.golgolex.golgocloud.common.service.ServiceEnvironment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * The CloudGroupProvider interface provides methods for retrieving, updating, deleting, and creating cloud groups.
 * It allows access to a list of CloudGroup objects and provides filtering based on ServiceEnvironment.
 */
public interface CloudGroupProvider {

    /**
     * Retrieves a list of CloudGroup objects.
     *
     * @return A list of CloudGroup objects.
     */
    List<CloudGroup> cloudGroups();

    /**
     * Retrieves an Optional that contains the CloudGroup with the specified name.
     *
     * @param name the name of the CloudGroup to retrieve
     * @return an Optional containing the CloudGroup with the specified name, or an empty Optional if not found
     */
    default Optional<CloudGroup> cloudGroup(@NotNull String name) {
        return cloudGroups().stream().filter(it -> it.name().equalsIgnoreCase(name)).findFirst();
    }

    /**
     * Filters and retrieves a list of CloudGroup objects based on the provided ServiceEnvironment.
     *
     * @param environment The ServiceEnvironment used to filter the CloudGroup list.
     * @return A list of CloudGroup objects that belong to the specified ServiceEnvironment.
     */
    default List<CloudGroup> cloudGroups(@NotNull ServiceEnvironment environment) {
        return this.cloudGroups().stream().filter(it -> it.serviceEnvironment().equals(environment)).toList();
    }

    /**
     * Reloads the groups configuration. If the groups configuration is present, adds the groups to the internal list of cloud groups.
     * If the groups configuration is not found, logs an error message.
     */
    void reloadGroups();

    /**
     * Updates the information of a cloud group.
     *
     * @param cloudGroup The cloud group to be updated.
     *
     * @see CloudGroupProvider#updateGroup(CloudGroup)
     */
    void updateGroup(@NotNull CloudGroup cloudGroup);

    /**
     * Deletes the specified CloudGroup.
     *
     * @param cloudGroup The CloudGroup to be deleted. Must not be null.
     */
    void deleteGroup(@NotNull CloudGroup cloudGroup);

    /**
     * Creates a new CloudGroup.
     *
     * @param cloudGroup the CloudGroup to be created
     */
    void createGroup(@NotNull CloudGroup cloudGroup);

}