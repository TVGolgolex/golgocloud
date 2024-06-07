package dev.golgolex.golgocloud.terminal.commands;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * The CommandService class is responsible for registering and executing commands.
 */
@Getter
@Accessors(fluent = true)
public final class CommandService {

    /**
     * Registers a command in the command service.
     */
    private final List<Object> commands = new ArrayList<>();

    /**
     * Registers a command in the command service.
     *
     * @param command the command to be registered
     */
    public void registerCommand(@NotNull Object command) {
        this.commands.add(command);
    }

    /**
     * Executes a command based on the given arguments.
     *
     * @param args the command arguments
     */
    @SneakyThrows
    public void call(@NotNull String[] args) {
        var main = args[0];
        for (var command : commands) {

            var mainCommand = command.getClass().getDeclaredAnnotation(Command.class);
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
                        try {
                            if (method.getParameterCount() == 0) {
                                System.out.println("Command executed: " + method.getName() + "," + command.getClass().getSimpleName() + " with no parameters");
                                method.invoke(command);
                            } else {
                                System.out.println("Command executed: " + method.getName() + "," + command.getClass().getSimpleName() + " with " + params.toArray().length + " parameters");
                                method.invoke(command, params.toArray());
                            }
                        } catch (Exception exception) {
                            System.err.println(exception.getMessage());
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the given arguments match the sub-command arguments specified in the SubCommand annotation.
     *
     * @param args        the command arguments
     * @param commandData the SubCommand annotation that contains the expected arguments
     * @return true if the arguments match, false otherwise
     */
    /*private static boolean isSubCommand(String[] args, SubCommand commandData) {
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
    }*/
    private static boolean isSubCommand(String[] args, SubCommand commandData) {
        var index = 0;
        var find = true;
        for (var s : Arrays.copyOfRange(args, 1, args.length)) {
            if ((args.length - 1) < index) {
                return find;
            }
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
