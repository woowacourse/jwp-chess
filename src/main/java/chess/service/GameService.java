package chess.service;

import chess.domain.Color;
import chess.domain.Result;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.dto.BoardDto;
import chess.dto.CommendDto;
import chess.dto.ResultDto;
import chess.repository.GameRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public BoardDto newBoard(int roomId) {
        Board board = new Board(new RegularRuleSetup(), Color.WHITE);
        return gameRepository.saveNewGame(roomId, board);
    }

    public void move(int boardId, CommendDto commendDto) {
        String source = commendDto.getSource();
        String target = commendDto.getTarget();
        Board board = gameRepository.findBoardByBoardId(boardId);
        board.move(source, target);
        gameRepository.movePiece(boardId, board, source, target);
    }

    public BoardDto loadGame(int roomId) {
        int boardId = gameRepository.findBoardIdByRoomId(roomId);
        Board board = gameRepository.findBoardByRoomId(roomId);
        return new BoardDto(boardId, board);
    }

    public BoardDto gameStateAndPieces(int boardId) {
        Board board = gameRepository.findBoardByBoardId(boardId);
        return new BoardDto(boardId, board);
    }

    public ResultDto gameResult(int boardId) {
        return getResultDto(gameRepository.findBoardByBoardId(boardId));
    }

    public ResultDto gameFinalResult(int boardId) {
        return getFinalResultDto(gameRepository.findBoardByBoardId(boardId));
    }

    private ResultDto getResultDto(Board board) {
        int whiteScore = (int) board.calculateScore(Color.WHITE);
        int blackScore = (int) board.calculateScore(Color.BLACK);
        return new ResultDto(whiteScore, blackScore, board.gameResult());
    }

    private ResultDto getFinalResultDto(Board board) {
        int whiteScore = (int) board.calculateScore(Color.WHITE);
        int blackScore = (int) board.calculateScore(Color.BLACK);
        Map<Result, Color> winner = new HashMap<>();
        winner.put(Result.WIN, board.winnersColor());
        return new ResultDto(whiteScore, blackScore, winner);
    }

}
