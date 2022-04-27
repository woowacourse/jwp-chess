package chess.service;

import org.springframework.stereotype.Service;

import chess.dto.BoardDto;
import chess.dto.MoveDto;
import chess.dto.ResultDto;
import chess.model.board.Board;
import chess.model.state.State;
import chess.model.state.finished.Status;
import chess.model.state.running.WhiteTurn;
import chess.repository.GameRepository;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(final GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public BoardDto start(Long id) {
        State state = new WhiteTurn(Board.init());
        gameRepository.deleteGameData(id);
        gameRepository.initGameData(id, state);
        return BoardDto.from(state.getBoard());
    }

    public BoardDto end(Long id) {
        Board board = gameRepository.getBoard(id);
        gameRepository.deleteGameData(id);
        return BoardDto.from(board.getBoard());
    }

    public BoardDto move(Long id, MoveDto moveDto) {
        State state = proceed(id, moveDto);
        return BoardDto.from(state.getBoard());
    }

    private State proceed(Long id, MoveDto moveDto) {
        State nowState = gameRepository.getState(id);
        State nextState = nowState.proceed(moveDto.command());
        gameRepository.saveGameData(id, nextState, moveDto);
        return nextState;
    }

    public ResultDto status(Long id) {
        Board board = gameRepository.getBoard(id);
        State status = new Status(board);
        return new ResultDto(status.getScores(), status.getWinner());
    }

    public BoardDto load(Long id) {
        Board board = gameRepository.getBoard(id);
        return BoardDto.from(board.getBoard());
    }
}
