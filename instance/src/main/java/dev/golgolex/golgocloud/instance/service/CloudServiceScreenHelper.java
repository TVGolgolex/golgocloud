package dev.golgolex.golgocloud.instance.service;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@UtilityClass
public class CloudServiceScreenHelper {

    /**
     * Executes a screen command in the specified screen session.
     *
     * @param sessionName the name of the screen session
     * @param command the command to execute
     */
    public void executeScreenCommand(@NotNull String sessionName, @NotNull String command) {
        try {
            Runtime.getRuntime().exec(new String[]{"screen", "-S", sessionName, "-X", "stuff", command + "\n"});
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checks if a screen session with the given session name exists.
     *
     * @param sessionName the name of the screen session to check
     * @return true if a screen session with the given session name exists, false otherwise
     */
    public boolean isScreenExisting(@NotNull String sessionName) {
        try {
            var process = Runtime.getRuntime().exec("screen -list".split(" "));
            try ( var reader = new BufferedReader(new InputStreamReader(process.getInputStream())) ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains(sessionName.toLowerCase())) {
                        return true;
                    }
                }
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

}
