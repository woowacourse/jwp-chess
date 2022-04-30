package chess.service;

import static java.util.stream.Collectors.toMap;

import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.entity.GameEntity;
import chess.entity.PieceEntity;
import chess.model.Color;
import chess.model.board.Board;
import chess.model.board.MoveResult;
import chess.model.board.Square;
import chess.model.game.ChessGame;
import chess.model.gamestatus.Status;
import chess.model.gamestatus.StatusType;
import chess.model.piece.Piece;
import chess.model.piece.PieceType;
import chess.service.dto.response.BoardDto;
import chess.service.dto.response.DeleteGameResponse;
import chess.service.dto.response.EndGameResponse;
import chess.service.dto.response.GameResultDto;
import chess.service.dto.response.GamesDto;
import chess.service.dto.response.MoveResponse;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class ChessService {
    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public void initGame(Integer gameId) {
        ChessGame chessGame = new ChessGame();
        pieceDao.initBoard(gameId);
        updateGame(chessGame, gameId);
    }

    private void updateGame(ChessGame chessGame, Integer id) {
        gameDao.update(new GameEntity(id, chessGame));
    }

    public MoveResponse move(Integer id, String from, String to) {
        ChessGame chessGame = getGameFromDao(id);
        MoveResult movedResult = chessGame.move(Square.of(from), Square.of(to));
        updatePiece(id, movedResult);
        updateGame(chessGame, id);
        return new MoveResponse(movedResult);
    }

    private void updatePiece(Integer id, MoveResult movedResult) {
        Map<Square, Piece> affectedPiece = movedResult.getAffectedPiece();
        for (Square square : affectedPiece.keySet()) {
            pieceDao.update(new PieceEntity(square, affectedPiece.get(square)), id);
        }
    }

    private ChessGame getGameFromDao(Integer id) {
        GameEntity game = gameDao.findById(id);
        Status status = getStatusFromDao(game.getStatus(), getBoardFromDao(id));
        return new ChessGame(Color.valueOf(game.getTurn()), status);
    }

    private Board getBoardFromDao(Integer id) {
        Map<Square, Piece> pieces = convertPieces(pieceDao.getBoardByGameId(id));
        return new Board(pieces);
    }

    private Map<Square, Piece> convertPieces(List<PieceEntity> pieceEntities) {
        return pieceEntities.stream()
                .collect(toMap(dto -> Square.of(dto.getSquare()), PieceType::createPiece));
    }

    private Status getStatusFromDao(String statusName, Board board) {
        return StatusType.createStatus(statusName, board);
    }

    public EndGameResponse endGame(Integer id) {
        ChessGame game = getGameFromDao(id);
        game.end();
        gameDao.update(new GameEntity(id, game));
        return new EndGameResponse(id);
    }

    public GamesDto getAllGames() {
        return new GamesDto(gameDao.findAll());
    }

    public void createGame(String name, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        gameDao.createGame(name, hashedPassword);
    }

    public GameResultDto getResult(Integer id) {
        return GameResultDto.of(getGameFromDao(id).getResult());
    }

    public BoardDto getBoard(Integer gameId) {
        return new BoardDto(pieceDao.getBoardByGameId(gameId));
    }

    public DeleteGameResponse deleteGame(Integer gameId, String password) {
        String hashedPassword = gameDao.findPasswordById(gameId);
        throwDifferentPassword(password, hashedPassword);
        ChessGame game = getGameFromDao(gameId);
        throwPlayingGame(game);
        gameDao.deleteGame(gameId);
        return new DeleteGameResponse(gameId, true);
    }

    private void throwPlayingGame(ChessGame game) {
        if (game.isPlaying()) {
            throw new IllegalArgumentException("게임 실행중에는 삭제할 수 없습니다.");
        }
    }

    private void throwDifferentPassword(String password, String hashedPassword) {
        if (!BCrypt.checkpw(password, hashedPassword)) {
            throw new IllegalArgumentException("암호가 다릅니다.");
        }
    }
}
