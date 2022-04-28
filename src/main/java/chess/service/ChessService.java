package chess.service;

import chess.domain.dao.BoardDao;
import chess.domain.dao.GameDao;
import chess.service.dto.GameDto;
import chess.service.dto.PieceDto;
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

    private final BoardDao boardDao;
    private final GameDao gameDao;
    private ChessBoard chessBoard = null;

    public ChessService(BoardDao boardDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }

    public long create(String title, String password){
        makeNewGame();
        int gameId = gameDao.create(chessBoard, title, Integer.parseInt(password));
        for (Map.Entry<String, ChessPiece> entry : chessBoard.convertToMap().entrySet()) {
            boardDao.save(
                    gameId,
                    getPosition(entry),
                    getPiece(entry),
                    getColor(entry));
        }
        return gameId;
    }

//    public void start() {
//        int lastGameId = gameDao.findLastGameId();
//        if (isNotSaved(lastGameId)) {
//            makeNewGame();
//            return;
//        }
//        loadLastGame(lastGameId);
//    }

    public void loadGame(int gameId){
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

    private void updateBoard(int gameId, String source, String target) {
        boardDao.updateMovePiece(gameId, source, target);
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

    public void move(String source, String target, int gameId) throws SQLException {
        ChessBoard chessBoard = findBoard(gameId);

        if (checkStatus(chessBoard, Status.END)) {
            end();
        }

        if (chessBoard.compareStatus(Status.PLAYING)) {
            chessBoard.move(new Position(source), new Position(target));
        }
        updateBoard(gameId, source, target);
        updateTurn(chessBoard.getCurrentTurn().name(),gameId);
    }

    private void updateTurn(String turn, int gameId) {
        gameDao.updateTurn(turn, gameId);
    }

    public Map<String, Double> status(ChessBoard chessBoard) {
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

    public Map<String, String> currentBoardForUI(ChessBoard chessBoard) {
        return chessBoard.convertToImageName();
    }

    public boolean checkStatus(ChessBoard chessBoard, Status status) {
        return chessBoard.compareStatus(status);
    }

    public List<GameDto> findAllGame() {
        return gameDao.findAll();
    }

    public ChessBoard findBoard(int gameId) {
        GameDto game = gameDao.findById(gameId);
        List<PieceDto> boardInfo = boardDao.findByGameId(gameId);
        HashMap<Position, ChessPiece> board = new HashMap<>();
        for (PieceDto pieceDto : boardInfo) {
            board.put(new Position(pieceDto.getPosition()),Type.from(pieceDto.getPiece()).createPiece(Color.from(pieceDto.getColor())));
        }
        return new ChessBoard(board, new Playing(), game.getTurn());
    }
}
