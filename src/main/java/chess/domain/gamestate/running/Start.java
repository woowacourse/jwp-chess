package chess.domain.gamestate.running;

import chess.domain.board.Board;
import chess.domain.gamestate.CommandType;
import chess.domain.gamestate.State;
import chess.domain.team.Team;
import chess.exception.domain.InvalidRoomCommandException;

public class Start extends Running {

    public Start(Board board) {
        super(board);
    }

    @Override
    public State changeCommand(CommandType command) {
        validateCommand(command);
        if (command == CommandType.MOVE) {
            return new Move(board);
        }
        throw new InvalidRoomCommandException("[ERROR] MOVE 명령만 가능합니다.");
    }

    private void validateCommand(CommandType command) {
        if (command == CommandType.START) {
            throw new InvalidRoomCommandException("[ERROR] 이미 게임이 시작되었습니다. 다시 시작할 수 없습니다.");
        }
        if (command == CommandType.END) {
            throw new InvalidRoomCommandException("[ERROR] 방금 시작된 게임입니다. 지금은 종료할 수 없습니다.");
        }
    }

    @Override
    public void processMove(String input, Team currentTeam) {
        throw new InvalidRoomCommandException("[ERROR] 현재 move 상태가 아닙니다.");
    }

    @Override
    public boolean isMove() {
        return false;
    }

    @Override
    public String getValue() {
        return "start";
    }
}
