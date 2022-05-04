package chess.service;

import chess.model.MoveResult;
import chess.model.entity.GameEntity;
import chess.model.entity.PieceEntity;
import chess.model.ChessGame;
import chess.model.GameResult;
import chess.model.Turn;
import chess.model.board.Board;
import chess.model.board.BoardFactory;
import chess.model.dao.GameDao;
import chess.model.dao.PieceDao;
import chess.model.dto.*;
import chess.model.piece.Piece;
import chess.model.piece.PieceFactory;
import chess.model.position.Position;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class ChessService {

    public static final String PIECE_NONE = "none-.";
    private final PieceDao pieceDao;
    private final GameDao gameDao;

    public ChessService(PieceDao pieceDao, GameDao gameDao) {
        this.pieceDao = pieceDao;
        this.gameDao = gameDao;
    }

    public Long start(RoomRequest roomRequest) {
        long gameId = initGame(roomRequest);
        initBoard(gameId);

        return gameId;
    }

    public GameInfosResponse getAllGames() {
        List<GameEntity> games = gameDao.findAll();
        return new GameInfosResponse(games.stream()
                .map(GameInfoResponse::from)
                .collect(Collectors.toList()));
    }

    public BoardResponse move(MoveRequest moveRequest, Long id) {
        ChessGame chessGame = findChessGameById(id);
        Turn turn = Turn.from(gameDao.findTurnById(id));
        MoveResult moveResult = chessGame.move(Position.from(moveRequest.getSource()), Position.from(moveRequest.getTarget()), turn);

        updateGame(moveResult, id, turn);

        if (chessGame.isKingDead()) {
            gameDao.update(turn.finish(), id);
        }

        return BoardResponse.from(chessGame.getBoard());
    }

    public BoardResponse getBoardByGameId(long gameId) {
        return BoardResponse.from(toBoard(pieceDao.findAllByGameId(gameId)));
    }

    public String getTurn(Long id) {
        return gameDao.findTurnById(id);
    }

    public boolean isKingDead(Long id) {
        ChessGame chessGame = findChessGameById(id);
        return chessGame.isKingDead();
    }

    public GameResult getResult(Long id) {
        ChessGame chessGame = findChessGameById(id);
        return chessGame.getWinningResult();
    }

    public void deleteByGameId(String confirmPassword, Long id) {
        String storedPassword = gameDao.findPwdById(id);
        Turn turn = Turn.from(gameDao.findTurnById(id));
        turn.validateEnd();

        if (confirmPassword.equals(storedPassword)) {
            pieceDao.deleteByGameId(id);
            gameDao.deleteById(id);
            return;
        }
        throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
    }

    private long initGame(RoomRequest roomRequest) {
        return gameDao.initGame(roomRequest.getRoomName(), roomRequest.getPassword());
    }

    private void initBoard(Long gameId) {
        pieceDao.init(BoardFactory.create(), gameId);
    }

    private void updateGame(MoveResult moveResult, Long id, Turn turn) {
        Piece sourcePiece = moveResult.getSourcePiece();
        Piece targetPiece = moveResult.getTargetPiece();
        pieceDao.updateByPositionAndGameId(moveResult.getSourcePosition(), PieceDao.getPieceName(sourcePiece), id);
        pieceDao.updateByPositionAndGameId(moveResult.getTargetPosition(), PieceDao.getPieceName(targetPiece), id);
        gameDao.update(turn.change().getThisTurn(), id);
    }

    private ChessGame findChessGameById(long gameId) {
        return new ChessGame(toBoard(pieceDao.findAllByGameId(gameId)));
    }

    private Board toBoard(List<PieceEntity> rawBoard) {
        return new Board(rawBoard.stream()
                .collect(Collectors.toMap(
                        piece -> Position.from(piece.getPosition()),
                        piece -> PieceFactory.create(piece.getName()))
                ));
    }
}
