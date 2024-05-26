package dev.golgolex.golgocloud.common.configuration;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Accessors(fluent = true)
public final class ConfigurationService {

    private final File configurationDirectory;
    private final List<ConfigurationClass> configurations = new ArrayList<>();

    public ConfigurationService(@NotNull File baseDirectory) {
        this.configurationDirectory = new File(baseDirectory, "configurations");
        if (!configurationDirectory.exists()) {
            var ignore = configurationDirectory.mkdirs();
        }
    }

    public ConfigurationService(@NotNull File baseDirectory, @NotNull String child) {
        this.configurationDirectory = new File(baseDirectory, child);
        if (!configurationDirectory.exists()) {
            var ignore = configurationDirectory.mkdirs();
        }
    }

    public void addConfiguration(@NotNull ConfigurationClass... configurationClasses) {
        for (var configurationClass : configurationClasses) {
            this.configurations.add(configurationClass);
            configurationClass.reload();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfigurationClass> T configuration(@NotNull String id) {
        return (T) this.configurations.stream().filter(it -> it.id().equalsIgnoreCase(id)).findFirst().orElse(null);
    }

    @SuppressWarnings("unchecked")
    public <T extends ConfigurationClass> Optional<T> configurationOptional(@NotNull String id) {
        return (Optional<T>) this.configurations.stream().filter(it -> it.id().equalsIgnoreCase(id)).findFirst();
    }
}
