package chess.service;

import chess.domain.dao.BoardDao;
import chess.domain.dao.GameDao;
import chess.domain.game.Color;
import chess.domain.game.Status;
import chess.domain.game.board.ChessBoard;
import chess.domain.game.board.ChessBoardFactory;
import chess.domain.game.status.End;
import chess.domain.game.status.GameStatus;
import chess.domain.game.status.Playing;
import chess.domain.piece.ChessPiece;
import chess.domain.piece.Type;
import chess.domain.position.Position;
import chess.service.dto.GameDto;
import chess.service.dto.PieceDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChessService {

    private final BoardDao boardDao;
    private final GameDao gameDao;

    public ChessService(BoardDao boardDao, GameDao gameDao) {
        this.boardDao = boardDao;
        this.gameDao = gameDao;
    }

    public long create(String title, String password) {
        ChessBoard chessBoard = makeNewGame();
        int gameId = gameDao.create(chessBoard, title, password);
        for (Map.Entry<String, ChessPiece> entry : chessBoard.convertToMap().entrySet()) {
            boardDao.save(
                    gameId,
                    getPosition(entry),
                    getPiece(entry),
                    getColor(entry));
        }
        return gameId;
    }

    private ChessBoard makeNewGame() {
        ChessBoard chessBoard = ChessBoardFactory.initBoard();
        chessBoard.changeStatus(new Playing());
        return chessBoard;
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

    public List<GameDto> findAllGame() {
        return gameDao.findAll();
    }

    public ChessBoard findBoard(int gameId) {
        Optional<GameDto> game = gameDao.findById(gameId);
        List<PieceDto> boardInfo = boardDao.findByGameId(gameId);
        HashMap<Position, ChessPiece> board = new HashMap<>();
        for (PieceDto pieceDto : boardInfo) {
            board.put(new Position(pieceDto.getPosition()), Type.from(pieceDto.getPiece()).createPiece(Color.from(pieceDto.getColor())));
        }
        return new ChessBoard(board, convertToGameStatus(game.get().getStatus()), game.get().getTurn());
    }

    private GameStatus convertToGameStatus(String status) {
        return  Status.valueOf(status).convertToGameStatus();
    }

    public void move(String source, String target, int gameId) throws SQLException {
        ChessBoard chessBoard = findBoard(gameId);

        if (chessBoard.compareStatus(Status.PLAYING)) {
            chessBoard.move(new Position(source), new Position(target));
        }
        boardDao.updateMovePiece(gameId, source, target);
        gameDao.updateTurn(chessBoard.getCurrentTurn().name(), gameId);

        if (checkStatus(chessBoard, Status.END)) {
            gameDao.updateStatus(gameId);
        }
    }

    public Map<String, Double> status(ChessBoard chessBoard) {
        return chessBoard.calculateScore().entrySet().stream()
                .collect(Collectors.toMap(m -> m.getKey().name(), Map.Entry::getValue));
    }

    public void end(int gameId) throws SQLException {
        boardDao.delete(gameId);
        gameDao.delete(gameId);
    }

    public String findWinner(ChessBoard chessBoard) {
        return chessBoard.decideWinner().name();
    }

    public boolean checkStatus(ChessBoard chessBoard, Status status) {
        return chessBoard.compareStatus(status);
    }

    public void deleteGame(int gameId, String password) throws IllegalArgumentException, SQLException {
        Optional<GameDto> gameDto = gameDao.findById(gameId);
        validateRemovable(password, gameDto);
        end(gameId);
    }

    private void validateRemovable(String password, Optional<GameDto> gameDto) {
        validateStatus(Status.valueOf(gameDto.get().getStatus()));
        validatePassword(gameDto.get(), password);
    }

    private void validateStatus(Status gameStatus) {
        if (gameStatus != Status.END) {
            throw new IllegalArgumentException("종료된 게임만 삭제할 수 있습니다");
        }
    }

    private void validatePassword(GameDto gameDto, String password) {
        if (!gameDto.getPassword().equals(password)) {
            throw new IllegalArgumentException("올바르지 않은 비밀번호입니다.");
        }
    }

    public Map<String, String> currentBoardForUI(ChessBoard chessBoard) {
        return chessBoard.convertToImageName();
    }
}
