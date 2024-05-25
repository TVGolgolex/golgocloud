package dev.golgolex.golgocloud.common;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

@UtilityClass
public final class FileHelper {

    /**
     * Copies data from an input stream to an output stream using a buffer.
     *
     * @param inputStream  The input stream to read from.
     * @param outputStream The output stream to write to.
     * @throws IOException If an I/O error occurs.
     */
    public void copy(
            @NotNull InputStream inputStream,
            @NotNull OutputStream outputStream)
            throws IOException {
        byte[] buffer = new byte[8192];
        copy(inputStream, outputStream, buffer);

        // Attempt to finalize the buffer using reflection to release resources.
        try {
            Method method = byte[].class.getMethod("finalize");
            method.setAccessible(true);
            method.invoke(buffer);
        } catch (Exception ignored) {
        }
    }

    /**
     * Copies data from an input stream to an output stream using a specified buffer.
     *
     * @param inputStream  The input stream to read from.
     * @param outputStream The output stream to write to.
     * @param buffer       The buffer to use for copying data.
     * @throws IOException If an I/O error occurs.
     */
    public void copy(
            @NotNull InputStream inputStream,
            @NotNull OutputStream outputStream,
            byte[] buffer)
            throws IOException {
        int len;
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
            outputStream.flush();
        }
    }

    /**
     * Copies a file to a directory.
     *
     * @param sourceFile The file to copy.
     * @param targetFile The directory to copy the file to.
     * @throws IOException If an I/O error occurs.
     */
    public void copyFileToDirectory(
            @NotNull File sourceFile,
            @NotNull File targetFile)
            throws IOException {
        copy(sourceFile.toPath(), new File(targetFile.getPath(), sourceFile.getName()).toPath());
    }

    /**
     * Copies a file from a source path to a target path using a default buffer size.
     *
     * @param sourcePath The path of the file to copy.
     * @param targetPath The path to copy the file to.
     * @throws IOException If an I/O error occurs.
     */
    public void copy(
            @NotNull Path sourcePath,
            @NotNull Path targetPath)
            throws IOException {
        copy(sourcePath, targetPath, new byte[16384]);
    }

    /**
     * Copies a file from a source path to a target path using a specified buffer size.
     *
     * @param sourcePath The path of the file to copy.
     * @param targetPath The path to copy the file to.
     * @param buffer     The buffer to use for copying data.
     * @throws IOException If an I/O error occurs.
     */
    public void copy(
            @NotNull Path sourcePath,
            @NotNull Path targetPath,
            byte[] buffer)
            throws IOException {
        // Check if the source file exists
        if (!Files.exists(sourcePath)) {
            System.err.println("No file found: " + sourcePath);
            return;
        }

        // Create parent directories for the target path if they don't exist
        if (!Files.exists(targetPath.getParent())) {
            System.err.println("Failed to create directory: " + targetPath.getParent());
            return;
        }

        // Delete existing target file if it exists
        Files.deleteIfExists(targetPath);

        // Create a new target file
        Files.createFile(targetPath);

        // Copy data from source to target using input and output streams
        try (var inputStream = Files.newInputStream(sourcePath);
             var outputStream = Files.newOutputStream(targetPath)) {
            copy(inputStream, outputStream, buffer);
        }
    }

    /**
     * Copies all files and directories from a source directory to a target directory.
     *
     * @param sourceDir The source directory to copy files from.
     * @param targetDir The target directory to copy files to.
     * @throws IOException If an I/O error occurs.
     */
    public void copyFilesInDirectory(
            @NotNull File sourceDir,
            @NotNull File targetDir)
            throws IOException {
        // Check if the source directory exists
        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            return;
        }

        // Create target directory if it doesn't exist
        if (!targetDir.exists()) {
            if (!targetDir.mkdirs()) {
                System.err.println("Failed to create directory: " + targetDir);
                return;
            }
        }

        // List files and directories in the source directory
        var files = sourceDir.listFiles();
        var buffer = new byte[16384];
        if (files != null) {
            // Iterate through each file or directory
            for (var file : files) {
                if (file == null) {
                    continue;
                }
                // If it's a directory, recursively copy its contents
                if (file.isDirectory()) {
                    copyFilesInDirectory(file, new File(targetDir, file.getName()));
                } else {
                    // If it's a file, copy it to the target directory
                    var n = new File(targetDir.getAbsolutePath() + '/' + file.getName());
                    copy(file.toPath(), n.toPath(), buffer);
                }
            }
        }
    }

    /**
     * Inserts data from a resource file into a target file.
     *
     * @param resourcePath The path to the resource file.
     * @param targetPath   The path to the target file.
     */
    public void insertData(
            @NotNull String resourcePath,
            @NotNull String targetPath
    ) {
        // Delete the target file if it exists
        var targetFile = new File(targetPath);
        if (targetFile.exists()) {
            if (!targetFile.delete()) {
                System.err.println("Failed to delete target file: " + targetPath);
                return;
            }
        }

        // Copy data from the resource file to the target file
        try (var inputStream = FileHelper.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Resource file not found: " + resourcePath);
                return;
            }

            var outputPath = Paths.get(targetPath);
            Files.copy(inputStream, outputPath);
        } catch (IOException e) {
            System.err.println("Failed to insert data: " + e.getMessage());
        }
    }

    /**
     * Deletes a directory and all its contents recursively.
     *
     * @param directory The directory to be deleted.
     */
    public void deleteDirectory(@NotNull File directory) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        var files = directory.listFiles();
        if (files != null) {
            for (var file : files) {
                if (file.isDirectory()) {
                    // Recursive call to delete subdirectories and files
                    deleteDirectory(file);
                } else {
                    // Delete file
                    if (!file.delete()) {
                        return; // Unable to delete file
                    }
                }
            }
        }

        // Delete the empty directory
        if (!directory.delete()) {
            System.err.println("Delete failed: " + directory);
        }
    }

    /**
     * Rewrites the content of a file, replacing a specific line with a new line.
     *
     * @param file The file to be rewritten.
     * @param host The new host value to be inserted.
     * @throws IOException If an I/O error occurs.
     */
    public void rewriteFileUtils(
            @NotNull File file,
            @NotNull String host
    ) throws IOException {
        // Check if file exists
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        }

        // Read file content and perform modifications
        var lines = new ArrayList<String>();
        var modified = false;
        try (var reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!modified && line.trim().startsWith("query_enabled")) {
                    // Insert the new line with the host value
                    lines.add("  host: " + host);
                    modified = true;
                } else {
                    // Add the original line to the list
                    lines.add(line);
                }
            }
        }

        // Write modified content back to the file
        try (var writer = new PrintWriter(new FileWriter(file))) {
            for (var line : lines) {
                writer.println(line);
            }
        }
    }

    public String readFile(
            @NotNull File file,
            @NotNull String prefix
    ) {
        try (var reader = new Scanner(file)) {
            var content = new StringBuilder(prefix);

            while (reader.hasNextLine())
                content.append(reader.nextLine()).append('\n');

            return content.toString();
        } catch (Exception exception) {
            return null;
        }
    }
}
