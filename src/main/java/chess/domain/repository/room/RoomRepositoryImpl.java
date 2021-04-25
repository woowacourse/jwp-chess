package chess.domain.repository.room;

import chess.domain.board.Board;
import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.dao.piece.PieceDao;
import chess.dao.room.RoomDao;
import chess.exception.domain.NotFoundException;
import chess.utils.BoardUtil;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepositoryImpl implements RoomRepository {

    private final PieceDao pieceDao;
    private final RoomDao roomDao;

    public RoomRepositoryImpl(PieceDao pieceDao, RoomDao roomDao) {
        this.pieceDao = pieceDao;
        this.roomDao = roomDao;
    }

    @Override
    public long insert(String name) {
        Board board = BoardUtil.generateInitialBoard();
        long roomId = roomDao.insert(
            new Room(0, name, new Ready(board), Team.WHITE)
        );
        for (Piece piece : board.getPieces()) {
            pieceDao.insert(roomId, piece);
        }
        return roomId;
    }

    @Override
    public long insert(Room room) {
        long roomId = roomDao.insert(room);

        Board board = room.getBoard();
        for (Piece piece : board.getPieces()) {
            pieceDao.insert(roomId, piece);
        }
        return roomId;
    }

    @Override
    public Room findByName(String name) {
        if (roomDao.isExistName(name)) {
            throw new NotFoundException("[ERROR] 존재하지 않는 방입니다.");
        }

        Room room = roomDao.findRoomByName(name);
        List<Piece> pieces = pieceDao.findPiecesByRoomId(room.getId());
        return new Room(
            room.getId(),
            name,
            State.generateState(room.getState().getValue(), Board.of(pieces)),
            room.getCurrentTeam()
        );
    }

    @Override
    public List<Room> findAll() {
        return roomDao.findAll();
    }

    @Override
    public void save(Room room) {
        roomDao.update(room);
    }

    @Override
    public void saveAfterMove(Room afterMoveRoom, String source, String target) {
        List<Piece> beforeMovePieces = pieceDao.findPiecesByRoomId(afterMoveRoom.getId());
        Board beforeMoveBoard = Board.of(beforeMovePieces);
        Piece sourcePiece = beforeMoveBoard.find(Location.of(source));

        roomDao.update(afterMoveRoom);
        List<Piece> afterMovePieces = afterMoveRoom.getBoard().getPieces();

        if (beforeMovePieces.size() != afterMovePieces.size()) {
            Piece removedPiece = beforeMovePieces
                .stream()
                .filter(piece -> piece.getLocation().equals(Location.of(target)))
                .filter(piece -> !piece.equals(sourcePiece))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("[ERROR] 존재하지 않는 기물입니다."));
            pieceDao.deletePieceById(removedPiece.getId());
        }

        for (Piece piece : afterMovePieces) {
            pieceDao.update(piece);
        }
    }

    @Override
    public boolean exists(String name) {
        return roomDao.isExistName(name);
    }

    @Override
    public void deleteAll() {
        pieceDao.deleteAll();
        roomDao.deleteAll();
    }
}
