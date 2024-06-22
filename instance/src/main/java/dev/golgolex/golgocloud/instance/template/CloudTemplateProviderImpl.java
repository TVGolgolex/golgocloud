package dev.golgolex.golgocloud.instance.template;

import dev.golgolex.golgocloud.common.FileHelper;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.golgocloud.common.template.CloudServiceTemplate;
import dev.golgolex.golgocloud.common.template.CloudTemplateProvider;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesReplyPacket;
import dev.golgolex.golgocloud.common.template.packets.CloudServiceTemplatesRequestPacket;
import dev.golgolex.golgocloud.instance.CloudInstance;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Getter
@Accessors(fluent = true)
public final class CloudTemplateProviderImpl implements CloudTemplateProvider {

    private final List<CloudServiceTemplate> cloudServiceTemplates = new ArrayList<>();
    private final File templatesDirectory;
    private final Path everyTemplateDirectory;
    private final Path everyServerTemplateDirectory;
    private final Path everyProxyTemplateDirectory;

    /**
     * Initializes a cloud template provider for a given instance directory.
     *
     * @param instanceDirectory the instance directory where the templates are stored
     */
    public CloudTemplateProviderImpl(File instanceDirectory) {
        this.templatesDirectory = new File(instanceDirectory, "/local/templates");
        this.everyTemplateDirectory = Paths.get(this.templatesDirectory.toString(), "/every");
        this.everyServerTemplateDirectory = Paths.get(this.everyTemplateDirectory.toString(), "/server");
        this.everyProxyTemplateDirectory = Paths.get(this.everyTemplateDirectory.toString(), "/proxy");
        try {
            this.createDirectoryIfNotExists(this.templatesDirectory.toPath());
            this.createDirectoryIfNotExists(this.everyTemplateDirectory);
            this.createDirectoryIfNotExists(this.everyServerTemplateDirectory);
            this.createDirectoryIfNotExists(this.everyProxyTemplateDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a directory if it does not already exist.
     *
     * @param path the path of the directory to create
     * @throws IOException if an I/O error occurs
     */
    private void createDirectoryIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    @Override
    public void reloadTemplates() {
        this.cloudServiceTemplates.clear();
        CloudServiceTemplatesReplyPacket reply = CloudInstance.instance().nettyClient().thisNetworkChannel().sendQuery(new CloudServiceTemplatesRequestPacket());
        this.cloudServiceTemplates.addAll(reply.cloudServiceTemplates());
    }

    @Override
    public void checkExistTemplates(@NotNull CloudService cloudService) {
        var groupTemplateDirectory = new File(this.templatesDirectory, cloudService.group());
        if (!Files.exists(groupTemplateDirectory.toPath())) {
            try {
                Files.createDirectories(groupTemplateDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        var templateBase = this.cloudServiceTemplate(cloudService.template(), cloudService.group());
        if (templateBase == null) {
            CloudInstance.instance().logger().warn("No template base for key: " + cloudService.template() + " found.");
            return;
        }

        var templateDirectory = new File(groupTemplateDirectory, cloudService.template());
        var pluginsDirectory = new File(templateDirectory, "plugins");
        if (!Files.exists(templateDirectory.toPath())) {
            try {
                Files.createDirectories(templateDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (!Files.exists(pluginsDirectory.toPath())) {
            try {
                Files.createDirectories(pluginsDirectory.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
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

    /**
     * Copies files from the given CloudService to the current CloudTemplateProviderImpl instance.
     * The method copies files from the everyTemplateDirectory, everyServerTemplateDirectory, and everyProxyTemplateDirectory
     * based on the provided CloudService's template and environment.
     *
     * @param source the CloudService instance to copy files from
     */
    public void copyFiles(@NotNull CloudService source) {
        this.copyTemplatesFiles(source);
        this.copyFilesFromEvery(source);
    }

    /**
     * Copies files from the specified {@link CloudService} source.
     *
     * @param source the source {@link CloudService} object
     * @throws RuntimeException if an {@link IOException} occurs during file copying
     */
    public void copyFilesFromEvery(@NotNull CloudService source) {
        /*var everyTemplateDirectory = new File(this.everyTemplateDirectory.toString(), source.template());
        try {
            FileHelper.copyFilesInDirectory(everyTemplateDirectory, Path.of(source.path()).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        switch (source.environment()) {
            case SERVER -> {
                var serverTemplateDirectory = new File(this.everyServerTemplateDirectory.toString(), source.template());
                try {
                    FileHelper.copyFilesInDirectory(serverTemplateDirectory, Path.of(source.path()).toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case PROXY -> {
                File proxyTemplateDirectory = new File(this.everyProxyTemplateDirectory.toString(), source.template());
                try {
                    FileHelper.copyFilesInDirectory(proxyTemplateDirectory, Path.of(source.path()).toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Copies template files from the specified CloudService to the corresponding destination directory.
     *
     * @param source the CloudService from which to copy the template files
     * @throws RuntimeException if an I/O error occurs during the file copying process
     */
    public void copyTemplatesFiles(@NotNull CloudService source) {
        this.checkExistTemplates(source);
        var templateDirectory = new File(new File(this.templatesDirectory, source.group()), source.template());
        try {
            FileHelper.copyFilesInDirectory(templateDirectory, Path.of(source.path()).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
