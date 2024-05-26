package dev.golgolex.golgocloud.common.template;

import dev.golgolex.golgocloud.common.service.CloudService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface CloudTemplateProvider {

    List<CloudTemplate> cloudTemplates();

    CloudTemplate cloudTemplate(@NotNull String id, @NotNull String group);

    List<CloudTemplate> cloudTemplates(@NotNull String group);

    void reloadTemplates();

    void insertTemplate(@NotNull CloudService cloudService, @NotNull String id);

}