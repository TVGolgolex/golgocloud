package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.quala.command.Command;
import dev.golgolex.quala.command.CommandSender;
import dev.golgolex.quala.command.DefaultCommand;
import org.jetbrains.annotations.NotNull;

@Command(command = "module", description = "Handle the modules", aliases = "modules")
public class ModuleCommand {

    @DefaultCommand
    public void onDefault(@NotNull CommandSender commandSender) {
        commandSender.sendMessage(new String[]{
                "modules reload",
                "modules reload <name>",
                "modules disable <name>"
        });
    }

}
