package dev.golgolex.golgocloud.instance.template;

import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.template.CloudTemplate;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public class CloudTemplateProviderImpl implements CloudTemplateProvider {

    private final List<CloudTemplate> cloudTemplates = new ArrayList<>();

    @Override
    public CloudTemplate cloudTemplate(@NotNull String id, @NotNull String group) {
        return null;
    }

    @Override
    public List<CloudTemplate> cloudTemplates(@NotNull String group) {
        return List.of();
    }

    @Override
    public void reloadTemplates() {

    }

    @Override
    public void insertTemplate(@NotNull CloudService cloudService, @NotNull String id) {

    }
}
