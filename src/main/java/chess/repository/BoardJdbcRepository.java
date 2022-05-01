package chess.repository;

import chess.domain.board.Board;
import chess.domain.board.BoardFactory;
import chess.domain.board.Position;
import chess.domain.piece.Piece;
import chess.dto.MoveRequest;
import chess.repository.dao.BoardDao;
import chess.repository.dao.RoomDao;
import chess.repository.entity.BoardEntity;
import chess.repository.entity.RoomEntity;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class BoardJdbcRepository implements BoardRepository {

    private final BoardDao boardDao;
    private final RoomDao roomDao;

    public BoardJdbcRepository(BoardDao boardDao, RoomDao roomDao) {
        this.boardDao = boardDao;
        this.roomDao = roomDao;
    }

    @Override
    public void save(long id) {
        boardDao.save(id);
    }

    @Override
    public Board findById(long id) {
        final Map<Position, Piece> boardData = boardDao.findById(id)
                .stream()
                .collect(Collectors.toMap(BoardEntity::getPosition, BoardEntity::getPiece));
        final RoomEntity roomEntity = roomDao.findById(id);
        return BoardFactory.newInstance(boardData, roomEntity.getTurn());
    }

    @Override
    public void updateMove(MoveRequest moveRequest) {
        boardDao.updateMove(moveRequest);
    }

    @Override
    public void deleteById(int id) {
        boardDao.deleteById(id);
    }

    @Override
    public void deleteAll() {
        boardDao.deleteAll();
    }
}
