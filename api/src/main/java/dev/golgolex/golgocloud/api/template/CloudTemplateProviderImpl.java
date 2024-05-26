package dev.golgolex.golgocloud.api.template;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudTemplateProviderImpl implements CloudTemplateProvider {

    private final List<CloudServiceTemplate> cloudServiceTemplates = new ArrayList<>();

    @Override
    public void checkExistTemplates(@NotNull CloudService cloudService) {

    }

    @Override
    public void updateCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void deleteCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void createCloudServiceTemplate(@NotNull CloudServiceTemplate cloudServiceTemplate) {

    }

    @Override
    public void reloadTemplates() {

    }
}
