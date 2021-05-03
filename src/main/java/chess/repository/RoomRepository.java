package chess.repository;

import chess.dao.piece.PieceDao;
import chess.dao.room.RoomDao;
import chess.domain.board.Board;
import chess.domain.game.Room;
import chess.domain.gamestate.State;
import chess.domain.gamestate.running.Ready;
import chess.domain.location.Location;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.exceptions.DuplicateRoomException;
import chess.exceptions.NoRoomException;
import chess.utils.BoardUtil;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RoomRepository {

    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    public RoomRepository(final RoomDao roomDao, final PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }

    public long create(String roomName) {
        if (roomDao.roomExists(roomName)) {
            throw new DuplicateRoomException("이미 존재하는 방입니다. 다른 이름을 사용해주세요.");
        }

        Room room = new Room(0, roomName, new Ready(BoardUtil.generateInitialBoard()), Team.WHITE);
        room.play("start");
        long roomId = roomDao.insert(room);
        Board board = BoardUtil.generateInitialBoard();
        for (Piece piece : board.getPieces()) {
            pieceDao.insert(roomId, piece);
        }
        return roomId;
    }

    public Room findByName(String roomName) {
        if (!roomDao.roomExists(roomName)) {
            throw new NoRoomException("존재하지 않는 방입니다.");
        }
        Room room = roomDao.findByName(roomName);
        List<Piece> pieces = pieceDao.findPiecesByRoomId(room.getId());
        return new Room(
                room.getId(),
                roomName,
                State.generateState(room.getState().getValue(), Board.of(pieces)),
                room.getCurrentTeam());
    }

    public void update(Room room) {
        roomDao.update(room);
    }

    public void updatePieceMove(Room room, String source, String target) {
        List<Piece> beforeMovePieces = pieceDao.findPiecesByRoomId(room.getId());

        Board board = room.getBoard();
        Piece sourcePiece = board.find(Location.of(source));
        roomDao.update(room);

        List<Piece> afterMovePieces = board.getPieces();
        if (beforeMovePieces.size() != afterMovePieces.size()) {
            Piece removedPiece = beforeMovePieces
                    .stream()
                    .filter(piece -> piece.getLocation().equals(Location.of(target)))
                    .filter(piece -> !piece.equals(sourcePiece))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기물입니다."));
            pieceDao.deletePieceById(removedPiece.getId());
        }

        for (Piece piece : board.getPieces()) {
            pieceDao.update(piece);
        }
    }

    public List<Room> findAll() {
        return roomDao.findAll();
    }
}
