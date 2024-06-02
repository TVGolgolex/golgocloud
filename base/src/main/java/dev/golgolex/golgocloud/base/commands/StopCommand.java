package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.terminal.commands.Command;
import dev.golgolex.golgocloud.terminal.commands.DefaultCommand;

@Command(command = "stop", description = "stop the cloud")
public class StopCommand {
    @DefaultCommand
    public void handle() {
        CloudBase.instance().logger().warn("Cloud has been stopped!");
        CloudBase.instance().shutdown(false);
    }
}