package chess.dao;

import chess.domain.Game;
import chess.domain.game.ChessBoard;
import chess.domain.game.ChessBoardInitializer;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.entities.GameEntity;
import chess.entities.MemberEntity;
import chess.entities.PieceEntity;
import chess.entities.PositionEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

    private final ChessBoardDao boardDao;
    private final ChessMemberDao memberDao;
    private final ChessPositionDao positionDao;
    private final ChessPieceDao pieceDao;

    public GameRepository(ChessBoardDao boardDao, ChessMemberDao memberDao, ChessPositionDao positionDao,
                          ChessPieceDao pieceDao) {
        this.boardDao = boardDao;
        this.memberDao = memberDao;
        this.positionDao = positionDao;
        this.pieceDao = pieceDao;
    }

    public List<GameEntity> findAll() {
        List<GameEntity> gameEntities = boardDao.findAll();
        List<GameEntity> gamesWithMemberEntities = new ArrayList<>();
        for (GameEntity gameEntity : gameEntities) {
            gamesWithMemberEntities.add(
                    new GameEntity(gameEntity.getId(), gameEntity.getRoomTitle(),
                            memberDao.getAllByBoardId(gameEntity.getId()), gameEntity.getPassword()));
        }
        return gamesWithMemberEntities;
    }

    public GameEntity save(GameEntity board) {
        final GameEntity savedBoard = boardDao.save(board);
        final Map<Position, Piece> initialize = new ChessBoardInitializer().initialize();
        positionDao.saveAll(savedBoard.getId());
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    savedBoard.getId());
            final Piece piece = initialize.get(position);
            pieceDao.save(new PieceEntity(piece.getColor(), piece.getType(), lastPositionId));
        }
        memberDao.saveAll(board.getMemberEntities(), savedBoard.getId());
        return savedBoard;
    }

    public GameEntity getRoomById(int id) {
        List<MemberEntity> memberEntities = memberDao.getAllByBoardId(id);
        Map<Position, Piece> positionsAndPieces = positionDao.findAllPositionsAndPieces(id);
        GameEntity gameEntity = boardDao.getById(id);
        return new GameEntity(gameEntity.getId(),
                gameEntity.getRoomTitle(),
                memberEntities,
                gameEntity.getPassword(),
                new Game(new ChessBoard(() -> positionsAndPieces), gameEntity.getGame().getTurn()));
    }

    public Map<String, Piece> getPiecesByRoomId(int roomId) {
        final Map<Position, Piece> allPositionsAndPieces = positionDao.findAllPositionsAndPieces(roomId);
        return allPositionsAndPieces.keySet().stream()
                .collect(Collectors.toMap(
                        position -> position.getRow().value() + position.getColumn().name(),
                        allPositionsAndPieces::get));
    }

    public void update(GameEntity gameEntity, Position sourceRawPosition, Position targetRawPosition) {
        PositionEntity sourcePosition = positionDao.getByColumnAndRowAndBoardId(sourceRawPosition.getColumn(),
                sourceRawPosition.getRow(), gameEntity.getId());
        PositionEntity targetPosition = positionDao.getByColumnAndRowAndBoardId(targetRawPosition.getColumn(),
                targetRawPosition.getRow(), gameEntity.getId());
        updateMovingPiecePosition(sourcePosition, targetPosition,
                gameEntity.getGame().getChessBoard().piece(targetRawPosition));
        boardDao.updateTurn(gameEntity.getGame().getTurn(), gameEntity.getId());
    }

    private void updateMovingPiecePosition(PositionEntity sourcePosition, PositionEntity targetPosition,
                                           Optional<Piece> targetPiece) {
        if (targetPiece.isPresent()) {
            pieceDao.deleteByPositionId(targetPosition.getId());
        }
        pieceDao.updatePositionId(sourcePosition.getId(), targetPosition.getId());
    }

    public boolean deleteByRoomIdAndPassword(int roomId, String password) {
        return boardDao.deleteByIdAndPassword(roomId, password) == 1;
    }
}
