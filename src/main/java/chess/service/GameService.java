package chess.service;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.model.board.Board;
import chess.model.state.State;
import chess.model.state.finished.Status;
import chess.model.state.running.WhiteTurn;
import chess.repository.GameRepository;
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
        return BoardDto.from(state.getBoard());
    }

    public BoardDto end(final String id) {
        Board board = gameRepository.getBoardFrom(id);
        gameRepository.deleteGameDataFrom(id);
        return BoardDto.from(board.getBoard());
    }

    public BoardDto move(final String id, final MoveDto moveDto) {
        State state = proceed(id, moveDto);
        return BoardDto.from(state.getBoard());
    }

    private State proceed(final String id, final MoveDto moveDto) {
        State nowState = gameRepository.getStateFrom(id);
        State nextState = nowState.proceed(moveDto.getCommand());
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
        return BoardDto.from(board.getBoard());
    }
}
