package dev.golgolex.golgocloud.base.commands;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.terminal.commands.Command;
import dev.golgolex.golgocloud.terminal.commands.DefaultCommand;

@Command(command = "reload", aliases = "rl", description = "reload the cloud")
public class ReloadCommand {
    @DefaultCommand
    public void handle() {
        var started = System.currentTimeMillis();
        CloudBase.instance().logger().warn("Cloud reload has been started!");
        CloudBase.instance().reload();
        CloudBase.instance().logger().success("Cloud reload has been finished &2(&1Took &3" + (System.currentTimeMillis() - started) + " &1ms&2)");
    }
}
