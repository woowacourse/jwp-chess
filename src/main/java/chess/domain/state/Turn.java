package chess.domain.state;

import chess.domain.chessboard.Board;
import chess.domain.game.Player;

public final class Turn extends Running {

    private final Player player;

    Turn(final Board board, final Player player) {
        super(board);
        this.player = player;
    }

    //TODO:명령어에 따라 상태를 분리하는 로직 리팩토링
    @Override
    public State proceed(final String command) {
        if (Command.END.startsWith(command)) {
            return new End(board);
        }
        if (!Command.MOVE.startsWith(command)) {
            throw new IllegalArgumentException("[ERROR] 올바른 명령이 아닙니다.");
        }

        movePiece(command);
        if (board.isEndSituation()) {
            return new End(board);
        }
        if (player.equals(Player.WHITE)) {
            return new Turn(board, Player.BLACK);
        }
        return new Turn(board, Player.WHITE);
    }

    @Override
    public Player getNextTurnPlayer() {
        return player.getOpponentPlayer();
    }
}
