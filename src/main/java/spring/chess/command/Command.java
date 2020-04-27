package spring.chess.command;

import spring.chess.progress.Progress;

public interface Command {
    Progress conduct();
}


