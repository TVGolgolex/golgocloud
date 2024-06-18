package dev.golgolex.golgocloud.plugin.paper.commands;

import dev.golgolex.golgocloud.cloudapi.CloudAPI;
import dev.golgolex.quala.command.Command;
import dev.golgolex.quala.command.CommandSender;
import dev.golgolex.quala.command.DefaultCommand;
import dev.golgolex.quala.command.SubCommand;
import dev.golgolex.quala.common.utils.executors.ExecutionUtils;

@Command(command = "join", description = "Join command to switch server", permission = "cloud.paper.command.join")
public class CommandJoin {

    @DefaultCommand
    public void onDefault(CommandSender commandSender) {
        commandSender.sendMessage(new String[]{
                "<yellow>/join list</yellow> <dark_gray>-</dark_gray> <gray>List all servers</gray>",
                "<yellow>/join transfer <server></yellow> <dark_gray>-</dark_gray> <gray>Transfer to a other server</gray>"
        });
    }

    @SubCommand(args = "list", ignoreArgs = "")
    public void onList(CommandSender commandSender) {
        commandSender.sendMessage("§7Current servers: §e" + CloudAPI.instance().cloudServiceProvider().cloudServices().size());
        new Thread(() -> {
            for (var cloudService : CloudAPI.instance().cloudServiceProvider().cloudServices()) {
                ExecutionUtils.ASYNC_EXECUTOR.execute(() -> {
                    var instance = CloudAPI.instance().cloudInstanceProvider().cloudInstance(cloudService.id());
                    commandSender.sendMessage(" §8- §e" + cloudService.id() + " §8- §7"
                            + cloudService.instance() + " §8- §7"
                            + instance.domain());
                });
            }
        });
    }

}
