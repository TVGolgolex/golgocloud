package dev.golgolex.golgocloud.instance.commands;

import dev.golgolex.golgocloud.instance.CloudInstance;
import dev.golgolex.quala.command.Command;
import dev.golgolex.quala.command.DefaultCommand;

@Command(command = "stop", description = "stop the cloud instance")
public class StopCommand {
    @DefaultCommand
    public void handle() {
        CloudInstance.instance().logger().warn("Cloud instance has been stopped!");
        CloudInstance.instance().shutdown(false);
    }
}
