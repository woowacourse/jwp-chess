package spring.chess.command;

import spring.chess.progress.Progress;

@FunctionalInterface
public interface CommandConductable {
    Progress couduct(String command);
}
