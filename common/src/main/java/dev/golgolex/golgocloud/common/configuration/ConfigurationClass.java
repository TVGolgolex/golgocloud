package dev.golgolex.golgocloud.common.configuration;

import dev.golgolex.quala.json.document.JsonDocument;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * ConfigurationClass is an abstract class that provides the foundation for creating and managing configurations.
 * It contains methods for creating and reloading configurations, as well as a constructor for initializing the class.
 */
@Getter
@Accessors(fluent = true)
public abstract class ConfigurationClass {

    private final String id;
    private final File configurationDirectory;
    private final Path fileDir;
    private JsonDocument configuration;

    /**
     * ConfigrationClass is an abstract class that provides the foundation for creating and managing configurations.
     * It contains methods for creating and reloading configurations, as well as a constructor for initializing the class.
     */
    public ConfigurationClass(
            @NotNull String id,
            @NotNull File configurationDirectory
    ) {
        this.id = id;
        this.configurationDirectory = configurationDirectory;
        if (!configurationDirectory.exists()) {
            var ignore = configurationDirectory.mkdirs();
        }
        this.fileDir = new File(this.configurationDirectory, id + ".json").toPath();
        this.reload();
    }

    /**
     * Create method is used to initialize the configuration by setting it to the default configuration and saving it to a file.
     * If any exception occurs during the process, it logs the exception message.
     */
    public void create() {
        this.configuration = defaultConfiguration();
        try {
            this.configuration.saveAsConfig(this.fileDir);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Retrieves the default configuration for the subclass.
     *
     * @return The default configuration as a JsonDocument object.
     */
    public abstract JsonDocument defaultConfiguration();

    /**
     * Reloads the configuration by loading the JSON document from the file directory. If the file does not exist,
     * it creates a new configuration using the default configuration provided by the subclass. If an error occurs
     * during loading or creation, it logs the exception message.
     */
    public void reload() {
        if (!Files.exists(this.fileDir)) {
            this.create();
        }
        try {
            this.configuration = JsonDocument.fromPath(this.fileDir);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }

    /**
     * Saves the configuration by calling the saveAsConfig method of the JsonDocument class.
     * If the file directory doesn't exist, it calls the create method to initialize and save the configuration.
     * If an error occurs during saving, it logs the exception message.
     */
    public void save() {
        try {
            this.configuration.saveAsConfig(this.fileDir);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
        }
    }
}
