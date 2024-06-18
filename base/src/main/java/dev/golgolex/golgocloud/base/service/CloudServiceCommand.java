package dev.golgolex.golgocloud.base.service;

import dev.golgolex.golgocloud.base.CloudBase;
import dev.golgolex.golgocloud.common.service.CloudService;
import dev.golgolex.quala.command.*;
import org.jline.reader.Candidate;

import java.util.List;
import java.util.stream.Collectors;

@Command(command = "service", aliases = "-s", description = "Manage the services")
public final class CloudServiceCommand {

    @DefaultCommand
    public void handle(CommandSender commandSender) {
        CloudBase.instance().logger().info("&3service &1list &2- &1list all services");
        CloudBase.instance().logger().info("&3service info &2<&1id&2> &2- &1shows all information");
        CloudBase.instance().logger().info("&3service terminate &2<&1id&2> &2- &1terminate the service");
    }

    @SubCommand(args = {"list"}, ignoreArgs = "")
    public void handleList(CommandSender commandSender) {
        CloudBase.instance().serviceProvider().cloudServices().stream().collect(Collectors.groupingBy(CloudService::group, Collectors.toList())).forEach((group, services) -> {
            String groupInfo = String.format("&3%s &2: (&1%d running services&2)", group, services.size());
            CloudBase.instance().logger().info(groupInfo);

            services.forEach(service -> {
                var serviceInfo = String.format(" &2- &3%s &2(&1uuid=%s&2;&1port=%s&2;&1template=%s&2)",
                        service.id(), service.uuid(), service.port(), service.template());
                CloudBase.instance().logger().info(serviceInfo);
            });
        });
    }

    @SubCommand(args = {"info", "<id>"}, ignoreArgs = "info")
    public void handleInfo(CommandSender commandSender, String id) {
        CloudBase.instance().serviceProvider().cloudService(id).ifPresentOrElse(cloudService -> {
            CloudBase.instance().logger().info("ID&2: &3" + cloudService.id());
            CloudBase.instance().logger().info("Group&2: &3" + cloudService.group());
            CloudBase.instance().logger().info("UUID&2: &3" + cloudService.uuid());
            CloudBase.instance().logger().info("Template&2: &3" + cloudService.template());
            CloudBase.instance().logger().info("Ready&2: &3" + cloudService.ready());
            CloudBase.instance().logger().info("Memory&2: &3" + cloudService.memory());
            CloudBase.instance().logger().info("ServiceNumber&2: &3" + cloudService.serviceNumber());
            CloudBase.instance().logger().info("Path&2: &3" + cloudService.path());
            CloudBase.instance().logger().info("Environment&2: &3" + cloudService.environment().name());
            CloudBase.instance().logger().info("Instance&2: &3" + cloudService.instance());
            CloudBase.instance().logger().info("GameID&2: &3" + cloudService.gameId());
            CloudBase.instance().logger().info("Host&2: &3" + cloudService.host());
            CloudBase.instance().logger().info("Port&2: &3" + cloudService.port());
            CloudBase.instance().logger().info("LifeCycle&2: &3" + cloudService.lifeCycle().name());
            CloudBase.instance().logger().info("MaximalPlayer&2: &3" + cloudService.maxPlayers());
            CloudBase.instance().logger().info("Connected players &2(&3" + cloudService.connected() + "&2)");
            for (var servicePlayer : cloudService.connected()) {
                CloudBase.instance().logger().info("&2- &1name&2=&3" + servicePlayer.name() + "&2;&1uuid&2=&3" + servicePlayer.uuid());
            }
            CloudBase.instance().logger().info("Online players &2(&3" + cloudService.online() + "&2)");
            for (var servicePlayer : cloudService.online()) {
                CloudBase.instance().logger().info("&2- &1name&2=&3" + servicePlayer.name() + "&2;&1uuid&2=&3" + servicePlayer.uuid());
            }
        }, () -> CloudBase.instance().logger().warn("Service &2'&1" + id + "&2' &1not found."));
    }

    @SubCommandCompleter(completionPattern = {"info", "<id>"}, subCommand = "info")
    public void completeInfoMethod(int index, List<Candidate> candidates) {
        if (index == 1) {
            candidates.addAll(CloudBase.instance().serviceProvider().cloudServices().stream().map(cloudService -> new Candidate(cloudService.id())).toList());
        }
    }

}
