package chess.service;

import chess.domain.dao.BoardJdbcTemplateDao;
import chess.domain.dao.GameJdbcTemplateDao;
import chess.domain.dto.GameDto;
import chess.domain.dto.PieceDto;
import chess.domain.dto.ResponseDto1;
import chess.domain.game.Color;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.game.status.End;
import chess.domain.game.status.Playing;
import chess.domain.piece.ChessPiece;
import chess.domain.piece.Type;
import chess.domain.position.Position;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private static final int EMPTY_RESULT = 0;
    // private final GameRawJdbcDao gameDao;

    private final BoardJdbcTemplateDao boardDao;
    private final GameJdbcTemplateDao gameDao;
    private ChessBoard chessBoard = null;

    public ChessService(BoardJdbcTemplateDao boardDao, GameJdbcTemplateDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }

    public void start() {
        int lastGameId = gameDao.findLastGameId();
        if (isNotSaved(lastGameId)) {
            makeNewGame();
            return;
        }
        loadLastGame(lastGameId);
    }

    private boolean isNotSaved(int lastGameId) {
        return lastGameId == EMPTY_RESULT;
    }

    private void makeNewGame() {
        chessBoard = ChessBoardFactory.initBoard();
        chessBoard.changeStatus(new Playing());
    }

    public void save() {
        int gameId = gameDao.save(chessBoard);
        for (Map.Entry<String, ChessPiece> entry : chessBoard.convertToMap().entrySet()) {
            boardDao.save(
                    gameId,
                    getPosition(entry),
                    getPiece(entry),
                    getColor(entry));
        }
    }

    private String getPosition(Map.Entry<String, ChessPiece> entry) {
        return entry.getKey();
    }

    private String getPiece(Map.Entry<String, ChessPiece> entry) {
        return entry.getValue().getName();
    }

    private String getColor(Map.Entry<String, ChessPiece> entry) {
        return entry.getValue().getColor().name();
    }

    private void loadLastGame(int lastGameId) {
        HashMap<Position, ChessPiece> board = new HashMap<>();
        for (PieceDto pieceDto : boardDao.findByGameId(lastGameId)) {
            ChessPiece piece = makePiece(pieceDto);
            board.put(new Position(pieceDto.getPosition()), piece);
        }
        GameDto game = gameDao.findById(lastGameId);
        chessBoard = new ChessBoard(board, new Playing(), game.getTurn());
    }

    private ChessPiece makePiece(PieceDto pieceDto) {
        return Type.from(pieceDto.getPiece()).createPiece(getPieceColor(pieceDto));
    }

    private Color getPieceColor(PieceDto pieceDto) {
        return Color.from(pieceDto.getColor());
    }

    public void move(String source, String target) {
        if (chessBoard.compareStatus(Status.PLAYING)) {
            chessBoard.move(new Position(source), new Position(target));
        }
    }

    public Map<String, Double> status() {
        return chessBoard.calculateScore().entrySet().stream()
                .collect(Collectors.toMap(m -> m.getKey().name(), Map.Entry::getValue));
    }

    public void end() throws SQLException {
        chessBoard.changeStatus(new End());
        boardDao.delete(gameDao.findLastGameId());
        gameDao.delete();
    }

    public String findWinner() {
        return chessBoard.decideWinner().name();
    }

    public Map<String, String> currentBoardForUI() {
        return chessBoard.convertToImageName();
    }

    public boolean checkStatus(Status status) {
        return chessBoard.compareStatus(status);
    }
}
