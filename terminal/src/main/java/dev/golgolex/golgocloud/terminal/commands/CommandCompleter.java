package dev.golgolex.golgocloud.terminal.commands;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.Arrays;
import java.util.List;

/**
 * CommandCompleter is a class that implements the Completer interface and provides auto-completion for commands.
 */
@AllArgsConstructor
public final class CommandCompleter implements Completer {

    /**
     * The CommandService class is responsible for registering and executing commands.
     */
    private final CommandService commandService;

    /**
     * Autocompletes command line input based on defined commands and their sub-commands.
     *
     * @param reader     The LineReader object used for reading user input.
     * @param line       The ParsedLine object representing the current command line input.
     * @param candidates The list of candidates that will be displayed to the user as auto-complete options.
     */
    @Override
    @SneakyThrows
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        var context = line.line().split(" ", -1);

        if (context.length == 0) {
            return;
        }

        var main = context[0];

        for (var command : this.commandService.commands()) {

            var data = command.getClass().getDeclaredAnnotation(Command.class);

            if (context.length == 1) {
                if (data.command().startsWith(main)) {
                    candidates.add(new Candidate(data.command()));
                }
            } else if (main.equalsIgnoreCase(data.command()) || Arrays.stream(data.aliases()).anyMatch(it -> it.equalsIgnoreCase(main))) {
                //sub commands
                var subIndex = line.wordIndex();
                var subCommand = Arrays.copyOfRange(context, 1, context.length);

                for (var completer : command.getClass().getDeclaredMethods()) {

                    if (!completer.isAnnotationPresent(SubCommandCompleter.class)) {
                        continue;
                    }

                    var subCompleter = completer.getDeclaredAnnotation(SubCommandCompleter.class);

                    if (subCompleter.completionPattern().length < subIndex) {
                        continue;
                    }

                    //todo check if previous args are the same layout

                    if (subCompleter.completionPattern()[subIndex - 1].startsWith("<") && (subCompleter.completionPattern()[subIndex - 1].endsWith(">"))) {
                        completer.invoke(command, subIndex, candidates);
                    } else {
                        candidates.add(new Candidate(subCompleter.completionPattern()[subIndex - 1]));
                    }
                }

                for (var completer : command.getClass().getDeclaredMethods()) {
                    if (!completer.isAnnotationPresent(SubCommand.class)) {
                        continue;
                    }

                    var subData = completer.getDeclaredAnnotation(SubCommand.class);
                    if (Arrays.stream(subData.args()).anyMatch(s -> s.startsWith("<") && s.endsWith(">"))) {
                        continue;
                    }

                    if (subData.args().length < subIndex) {
                        continue;
                    }

                    var possibleSubCommand = subData.args()[subIndex - 1];
                    candidates.add(new Candidate(possibleSubCommand));
                    break;
                }
            }
        }
    }
}
