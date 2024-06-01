package dev.golgolex.golgocloud.terminal.commands;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.util.*;

@Getter
@Accessors(fluent = true)
public final class CommandService {

    private final List<Object> commands = new ArrayList<>();

    public void registerCommand(Object command) {
        this.commands.add(command);
    }

    @SneakyThrows
    public void call(String[] args) {
        var main = args[0];
        for (var command : commands) {

            var mainCommand = command.getClass().getDeclaredAnnotation(Command.class);;

            if (mainCommand == null) {
                continue;
            }

            if (!(main.equalsIgnoreCase(mainCommand.command()) || Arrays.stream(mainCommand.aliases()).anyMatch(it -> it.equalsIgnoreCase(main)))) {
                continue;
            }

            for (var method : command.getClass().getDeclaredMethods()) {
                if (args.length == 1 && method.isAnnotationPresent(DefaultCommand.class)) {
                    method.invoke(command);
                    continue;
                }

                if (!method.isAnnotationPresent(SubCommand.class)) {
                    continue;
                }

                var commandData = method.getDeclaredAnnotation(SubCommand.class);
                if ((commandData.args().length + 1) == args.length) {
                    boolean find = isSubCommand(args, commandData);
                    if (find) {
                        var params = new LinkedList<>();

                        for (var parameter : method.getParameters()) {
                            var index = -1;
                            var argIndex = 0;
                            for (var arg : commandData.args()) {
                                if (arg.substring(1, arg.length() - 1).equalsIgnoreCase(parameter.getName())) {
                                    index = argIndex;
                                    break;
                                }
                                argIndex++;
                            }

                            if (index == -1) {
                                continue;
                            }

                            if (parameter.getType().equals(Integer.class) || parameter.getType().equals(int.class)) {
                                params.add(Integer.parseInt(args[index + 1]));
                            } else {
                                params.add(parameter.getType().cast(args[index + 1]));
                            }
                        }
                        method.invoke(command, params.toArray());
                    }
                }
            }
        }
    }

    private static boolean isSubCommand(String[] args, SubCommand commandData) {
        var index = 0;
        var find = true;
        for (var s : Arrays.copyOfRange(args, 1, args.length)) {
            var subPart = commandData.args()[index];
            if (subPart.startsWith("<") && subPart.endsWith(">")) {
                index++;
                continue;
            }
            if (!Objects.equals(subPart, s)) {
                find = false;
            }
            index++;
        }
        return find;
    }
}
