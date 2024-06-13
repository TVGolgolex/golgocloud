package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.quala.command.Command;
import dev.golgolex.quala.command.DefaultCommand;
import dev.golgolex.quala.command.CommandSender;

@Command(command = "stop", description = "stop the cloud")
public class StopCommand {
    @DefaultCommand
    public void handle(CommandSender commandSender) {
        CloudBase.instance().logger().warn("Cloud has been stopped!");
        CloudBase.instance().shutdown(false);
    }
}