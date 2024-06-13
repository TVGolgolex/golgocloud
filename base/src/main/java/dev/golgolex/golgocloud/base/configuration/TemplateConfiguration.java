package dev.golgolex.golgocloud.base.configuration;

import com.google.common.reflect.TypeToken;
import dev.golgolex.golgocloud.common.configuration.ConfigurationClass;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.quala.common.json.JsonDocument;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TemplateConfiguration extends ConfigurationClass {

    public TemplateConfiguration(@NotNull File configurationDirectory) {
        super("template", configurationDirectory);
    }

    @Override
    public JsonDocument defaultConfiguration() {
        return new JsonDocument()
                .write("service-templates", new ArrayList<CloudServiceTemplate>());
    }

    public List<CloudServiceTemplate> serviceTemplates() {
        return this.configuration().readObject("service-templates", new TypeToken<List<CloudServiceTemplate>>() {
        }.getType());
    }

    public void serviceTemplates(List<CloudServiceTemplate> cloudServiceTemplates) {
        this.configuration().write("service-templates", cloudServiceTemplates);
    }
}
