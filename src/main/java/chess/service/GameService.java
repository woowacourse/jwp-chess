package chess.service;

import chess.dto.response.BoardResponse;
import chess.dto.request.MoveRequest;
import chess.dto.response.ResultResponse;
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

    public BoardResponse start(final String id) {
        State state = new WhiteTurn(Board.init());
        gameRepository.initGameData(id, state);
        return convertToBoardDto(state.getBoard());
    }

    public BoardResponse end(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        gameRepository.deleteGameDataFrom(id);
        return convertToBoardDto(board.getBoard());
    }

    public BoardResponse move(final String id, final MoveRequest moveRequest) {
        State state = proceed(id, moveRequest);
        return convertToBoardDto(state.getBoard());
    }

    private State proceed(final String id, final MoveRequest moveRequest) {
        State nowState = gameRepository.getStateFrom(id);
        State nextState = nowState.proceed(moveRequest);
        gameRepository.saveGameData(id, nextState, moveRequest);
        return nextState;
    }

    public ResultResponse status(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        State status = new Status(board);
        return new ResultResponse(status.getScores(), status.getWinner());
    }

    public BoardResponse load(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        return convertToBoardDto(board.getBoard());
    }

    private BoardResponse convertToBoardDto(final Map<Position, Piece> board) {
        Map<String, String> squares = new HashMap<>();
        for (Position position : board.keySet()) {
            squares.put(position.getKey(), Piece.getKey(board.get(position)));
        }
        return BoardResponse.from(squares);
    }
}
