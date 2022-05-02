package chess.dao;

import chess.domain.Game;
import chess.domain.game.ChessBoard;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import chess.entities.GameEntity;
import chess.entities.MemberEntity;
import chess.entities.PositionEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public GameEntity getById(int id) {
        List<MemberEntity> memberEntities = memberDao.getAllByBoardId(id);
        Map<Position, Piece> positionsAndPieces = positionDao.findAllPositionsAndPieces(id);
        GameEntity gameEntity = boardDao.getById(id);
        return new GameEntity(gameEntity.getId(),
                gameEntity.getRoomTitle(),
                memberEntities,
                gameEntity.getPassword(),
                new Game(new ChessBoard(() -> positionsAndPieces), gameEntity.getGame().getTurn()));
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

    public void update(GameEntity gameEntity, Position sourceRawPosition, Position targetRawPosition) {
        PositionEntity sourcePosition = positionDao.getByColumnAndRowAndBoardId(sourceRawPosition.getColumn(),
                sourceRawPosition.getRow(), gameEntity.getId());
        PositionEntity targetPosition = positionDao.getByColumnAndRowAndBoardId(targetRawPosition.getColumn(),
                targetRawPosition.getRow(), gameEntity.getId());
        updateMovingPiecePosition(sourcePosition, targetPosition,
                gameEntity.getGame().getChessBoard().piece(targetRawPosition));
        changeTurn(gameEntity);
    }

    private void updateMovingPiecePosition(PositionEntity sourcePosition, PositionEntity targetPosition,
                                           Optional<Piece> targetPiece) {
        if (targetPiece.isPresent()) {
            pieceDao.deleteByPositionId(targetPosition.getId());
        }
        pieceDao.updatePositionId(sourcePosition.getId(), targetPosition.getId());
    }

    private void changeTurn(GameEntity gameEntity) {
        boardDao.updateTurn(gameEntity.getGame().getTurn(), gameEntity.getId());
    }
}
