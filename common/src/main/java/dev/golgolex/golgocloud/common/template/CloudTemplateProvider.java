package dev.golgolex.golgocloud.common.template;

import dev.golgolex.golgocloud.common.service.CloudService;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Represents a provider for cloud service templates.
 */
public interface CloudTemplateProvider {

    /**
     * Retrieves a list of cloud service templates.
     *
     * @return a list of {@link CloudServiceTemplate}
     */
    List<CloudServiceTemplate> cloudServiceTemplates();

    /**
     * Retrieves a {@link CloudServiceTemplate} by its ID and group name.
     *
     * @param id    the ID of the template
     * @param group the group name of the template
     * @return the matching {@link CloudServiceTemplate}, or null if not found
     */
    default CloudServiceTemplate cloudServiceTemplate(@NotNull String id, @NotNull String group) {
        return cloudServiceTemplates().stream().filter(cloudServiceTemplate -> cloudServiceTemplate.groupName().equalsIgnoreCase(group))
                .filter(cloudServiceTemplate -> cloudServiceTemplate.id().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    /**
     * Retrieves a list of CloudServiceTemplates matching the specified group.
     *
     * @param group the group name to filter the CloudServiceTemplates by
     * @return a list of CloudServiceTemplates matching the specified group
     */
    default List<CloudServiceTemplate> cloudServiceTemplates(@NotNull String group) {
        return cloudServiceTemplates().stream().filter(cloudServiceTemplate -> cloudServiceTemplate.groupName().equalsIgnoreCase(group)).toList();
    }

    /**
     * Filters and returns a list of CloudServiceTemplates based on the provided instanceId.
     *
     * @param instanceId the UUID of the instance to filter by
     * @return a list of CloudServiceTemplates containing the provided instanceId
     */
    default List<CloudServiceTemplate> cloudServiceTemplates(@NotNull UUID instanceId) {
        return cloudServiceTemplates().stream().filter(cloudServiceTemplate -> cloudServiceTemplate.instances().contains(instanceId)).toList();
    }

    /**
     * Checks if the templates for the given CloudService exist. If not, creates the necessary directories.
     *
     * @param cloudService the CloudService to check templates for
     */
    void checkExistTemplates(@NotNull CloudService cloudService);

    /**
     * Updates the provided {@link CloudServiceTemplate} in the cloud service template provider.
     *
     * @param cloudServiceTemplate the cloud service template to update
     */
    void updateCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate);

    /**
     * Deletes a CloudServiceTemplate.
     *
     * @param cloudServiceTemplate the CloudServiceTemplate to be deleted
     */
    void deleteCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate);

    /**
     * Creates a new cloud service template.
     *
     * @param cloudServiceTemplate the cloud service template to be created
     */
    void createCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate);

    /**
     * Reloads the templates from the configuration.
     */
    void reloadTemplates();

}