package wooteco.chess.service;

import chess.domain.GameResult;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.command.MoveCommand;
import chess.domain.piece.Piece;
import chess.dto.BoardDto;
import chess.dto.CellManager;
import chess.dto.TurnDto;
import org.springframework.stereotype.Service;
import wooteco.chess.entity.PieceEntity;
import wooteco.chess.repository.BoardRepository;
import wooteco.chess.repository.TurnRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ChessGameService {
    private BoardRepository boardRepository;
    private TurnRepository turnRepository;

    public ChessGameService(BoardRepository boardRepository, TurnRepository turnRepository) {
        this.boardRepository = boardRepository;
        this.turnRepository = turnRepository;
    }

    public Map<String, Object> loadBoard() {
        ChessBoard chessBoard = createBoardFromDb();
        return createModel(chessBoard);
    }

    public Map<String, Object> createNewChessGame() {
        ChessBoard chessBoard = new ChessBoard();
        Map<Position, Piece> board = chessBoard.getBoard();

        this.turnRepository.deleteAll();
        for (Position position : board.keySet()) {
            this.boardRepository.insert(position.getName(), board.get(position).getName());
        }
        this.turnRepository.insert(TurnDto.from(chessBoard).getCurrentTeam());

        return createModel(chessBoard);
    }

    public Map<String, Object> movePiece(String source, String target) {
        ChessBoard chessBoard = createBoardFromDb();
        Map<Position, Piece> board = chessBoard.getBoard();
        chessBoard.move(new MoveCommand(String.format("move %s %s", source, target)));

        this.boardRepository.deleteAll();
        for (Position position : board.keySet()) {
            this.boardRepository.insert(position.getName(), board.get(position).getName());
        }
        this.turnRepository.update(TurnDto.from(chessBoard).getCurrentTeam());

        return createModel(chessBoard);
    }

    public void endGame() {
        this.boardRepository.deleteAll();
        this.turnRepository.deleteAll();
    }

    public Map<String, Object> findWinner() {
        Map<String, Object> model = new HashMap<>();

        ChessBoard chessBoard = createBoardFromDb();

        GameResult gameResult = chessBoard.createGameResult();
        model.put("winner", gameResult.getWinner());
        model.put("loser", gameResult.getLoser());
        model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
        model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

        endGame();
        return model;
    }

    public boolean isGameOver() {
        ChessBoard chessBoard = createBoardFromDb();
        return chessBoard.isGameOver();
    }

    public boolean isNotGameOver() {
        return !isGameOver();
    }

    private Map<String, Object> createModel(ChessBoard chessBoard) {
        Map<String, Object> model = new HashMap<>();
        GameResult gameResult = chessBoard.createGameResult();
        CellManager cellManager = new CellManager();

        model.put("cells", cellManager.createCells(chessBoard));
        model.put("currentTeam", chessBoard.getTeam().getName());
        model.put("blackScore", gameResult.getAliveBlackPieceScoreSum());
        model.put("whiteScore", gameResult.getAliveWhitePieceScoreSum());

        return model;
    }

    private ChessBoard createBoardFromDb() {
        List<PieceEntity> pieceEntities = this.boardRepository.findAll();
        if (pieceEntities.isEmpty()) {
            throw new NoSuchElementException("테이블의 행이 비었습니다!!");
        }
        BoardDto boardDto = new BoardDto(pieceEntities);

        TurnDto turnDto = TurnDto.from(this.turnRepository.findFirst());

        return new ChessBoard(boardDto.createBoard(), turnDto.createTeam());
    }
}
