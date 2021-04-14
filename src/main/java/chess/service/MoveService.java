package chess.service;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.Position;
import chess.exception.CommandFormatException;
import chess.repository.GameRepository;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import org.springframework.stereotype.Service;
import spark.Response;

@Service
public class MoveService {
    private static final String POSITION_FORMAT = "[a-h][1-8]";

    private final GameRepository gameRepository;

    public MoveService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Object move(String gameId, String source, String target) {
        ChessGame chessGame = gameRepository.findByGameIdFromCache(gameId);

        validateRightInputs(source, target);
        Position sourcePosition = getPositionFromInput(source);
        Position targetPosition = getPositionFromInput(target);

        return executeMove(sourcePosition, targetPosition, chessGame);
    }

    private Object executeMove(Position sourcePosition, Position targetPosition, ChessGame chessGame) {
        chessGame.move(sourcePosition, targetPosition);

        return new GameDto(chessGame);
    }

    private void validateRightInputs(String source, String target) {
        if (isRightPositionFormat(source) && isRightPositionFormat(target)) {
            return;
        }

        throw new CommandFormatException();
    }

    private Position getPositionFromInput(String input) {
        String[] inputs = input.split("");

        int column = inputs[0].charAt(0) - 'a';
        int row = Board.getRow() - Integer.parseInt(inputs[1]);

        return new Position(row, column);
    }

    private boolean isRightPositionFormat(String inputs) {
        return inputs.matches(POSITION_FORMAT);
    }

}
