package chess.service;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.model.board.Board;
import chess.model.piece.Piece;
import chess.model.position.Position;
import chess.model.state.State;
import chess.model.state.finished.Status;
import chess.model.state.running.WhiteTurn;
import chess.repository.GameRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public BoardDto start(final String id) {
        State state = new WhiteTurn(Board.init());
        gameRepository.initGameData(id, state);
        return convertToBoardDto(state.getBoard());
    }

    public BoardDto end(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        gameRepository.deleteGameDataFrom(id);
        return convertToBoardDto(board.getBoard());
    }

    public BoardDto move(final String id, final MoveDto moveDto) {
        State state = proceed(id, moveDto);
        return convertToBoardDto(state.getBoard());
    }

    private State proceed(final String id, final MoveDto moveDto) {
        State nowState = gameRepository.getStateFrom(id);
        State nextState = nowState.proceed(moveDto);
        gameRepository.saveGameData(id, nextState, moveDto);
        return nextState;
    }

    public ResultDto status(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        State status = new Status(board);
        return new ResultDto(status.getScores(), status.getWinner());
    }

    public BoardDto load(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        return convertToBoardDto(board.getBoard());
    }

    private BoardDto convertToBoardDto(final Map<Position, Piece> board) {
        Map<String, String> squares = new HashMap<>();
        for (Position position : board.keySet()) {
            squares.put(position.getKey(), Piece.getKey(board.get(position)));
        }
        return BoardDto.from(squares);
    }
}
