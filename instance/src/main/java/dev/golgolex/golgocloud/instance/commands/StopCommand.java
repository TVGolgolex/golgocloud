package dev.golgolex.golgocloud.instance.commands;

import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.golgocloud.terminal.commands.Command;
import dev.golgolex.golgocloud.terminal.commands.DefaultCommand;

@Command(command = "stop", aliases = "!?", description = "Stop the instance of the cloud system")
public class StopCommand {
    @DefaultCommand
    public void handle() {
        CloudInstance.instance().logger().warn("Cloud instance has been stopped!");
        CloudInstance.instance().shutdown(false);
    }
}
