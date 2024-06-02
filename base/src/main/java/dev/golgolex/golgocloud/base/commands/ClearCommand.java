package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.terminal.commands.Command;
import dev.golgolex.golgocloud.terminal.commands.DefaultCommand;

@Command(command = "clear", description = "clear the cloud")
public class ClearCommand {
    @DefaultCommand
    public void onDefault() {
        CloudBase.instance().cloudTerminal().clear();
    }
}
