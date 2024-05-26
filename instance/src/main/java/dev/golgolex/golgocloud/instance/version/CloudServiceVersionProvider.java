package dev.golgolex.golgocloud.instance.version;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudServiceVersionProvider {

    private final File versionsDirectory;
    private final File paperVersionsDirectory;
    private final File proxyVersionsDirectory;

    public CloudServiceVersionProvider(@NotNull File instanceDirectory) {
        this.versionsDirectory = new File(instanceDirectory, "/local/versions");
        this.paperVersionsDirectory = new File(this.versionsDirectory, "paper-versions");
        this.proxyVersionsDirectory = new File(this.versionsDirectory, "proxy-versions");
        this.checkOrCreate(this.versionsDirectory.toPath());
        this.checkOrCreate(this.paperVersionsDirectory.toPath());
        this.checkOrCreate(this.proxyVersionsDirectory.toPath());
    }

    /**
     * Checks if the given path exists. If it doesn't exist, creates the directories
     * leading up to the path.
     *
     * @param path The path to check or create.
     */
    private void checkOrCreate(@NotNull Path path) {
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves the list of versions available in the given file directory.
     *
     * @param file The directory where the versions are stored.
     * @return A list of version names.
     */
    public List<String> listVersions(@NotNull File file) {
        var versions = new ArrayList<String>();
        if (file.exists()) {
            var files = file.listFiles();
            if (files != null) {
                for (var f : files) {
                    if (f.isFile() && f.getName().endsWith(".jar")) {
                        versions.add(f.getName().replace(".jar", ""));
                    }
                }
            }
        }
        return versions;
    }

    /**
     * Checks if a specific version exists in the given file directory.
     * The method retrieves a list of versions from the provided file directory,
     * and then checks if there is a version that matches the specified key,
     * ignoring case sensitivity.
     *
     * @param file the directory containing the versions
     * @param key the version to check for existence
     * @return true if the version exists, false otherwise
     */
    public boolean existVersion(@NotNull File file, @NotNull String key) {
        return this.listVersions(file).stream().anyMatch(key::equalsIgnoreCase);
    }

}