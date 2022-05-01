package chess.web.service;

import chess.domain.board.piece.Pieces;
import chess.domain.entity.Room;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class RoomService {

    private final RoomDao roomDao;
    private final PieceDao pieceDao;

    @Autowired
    public RoomService(RoomDao roomDao, PieceDao pieceDao) {
        this.roomDao = roomDao;
        this.pieceDao = pieceDao;
    }


    @Transactional
    public Long createRoom(String title, String password) {
        validateDuplicate(title);
        Long id = roomDao.save(title, password);
        pieceDao.save(Pieces.createInit().getPieces(), id);

        return id;
    }

    private void validateDuplicate(String title) {
        if (roomDao.existByTitle(title)) {
            throw new IllegalArgumentException("이미 존재하는 방 제목입니다.");
        }
    }

    @Transactional
    public void delete(String password, Long id) {
        Room room = roomDao.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        validateCorrectPassword(password, room);
        Pieces pieces = Pieces.from(pieceDao.findAllByBoardId(id));
        validateProceedingGame(pieces);
        roomDao.deleteById(id);
    }

    private void validateProceedingGame(Pieces pieces) {
        if (pieces.countOfKing() == 2) {
            throw new RuntimeException("게임이 끝나지 않아서 삭제할수 없습니다.");
        }
    }

    private void validateCorrectPassword(String password, Room room) {
        if (room.isNotSamePassword(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
    }

    public List<Room> getRoomList() {
        return roomDao.findAll();
    }

    public void checkJoinRoom(String password, Long id) {
        Room room = roomDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 방을 찾을수가 없습니다."));
        validateCorrectPassword(password, room);
    }
}
