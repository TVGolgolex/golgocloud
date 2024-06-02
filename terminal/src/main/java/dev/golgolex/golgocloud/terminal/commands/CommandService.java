/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                        break;
                    }
                }
            }
        }
    }

    private static boolean isSubCommand(String[] args, SubCommand commandData) {
        if (args.length == 0) {
            return false;
        }
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
