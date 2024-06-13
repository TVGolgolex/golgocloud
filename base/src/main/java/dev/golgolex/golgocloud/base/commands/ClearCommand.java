package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.quala.command.Command;
import dev.golgolex.quala.command.CommandSender;
import dev.golgolex.quala.command.DefaultCommand;

@Command(command = "clear", description = "clear the cloud")
public class ClearCommand {
    @DefaultCommand
    public void onDefault(CommandSender commandSender) {
        CloudBase.instance().cloudTerminal().clear();
    }
}
