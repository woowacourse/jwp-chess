package chess.service;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.CurrentTurnDto;
import chess.dto.RoomCreationRequestDto;
import chess.dto.RoomDeletionRequestDto;
import chess.dto.RoomResponseDto;
import chess.dto.RoomStatusDto;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    private final RoomDao roomDao;

    public RoomService(final RoomDao roomDao) {
        this.roomDao = roomDao;
    }

    public List<RoomResponseDto> findAll() {
        return roomDao.findAll();
    }

    public boolean isExistRoom(final int roomId) {
        return roomDao.isExistId(roomId);
    }

    public int createRoom(final RoomCreationRequestDto dto) {
        final String hashPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        return roomDao.save(dto.getRoomName(), GameStatus.READY, Color.WHITE, hashPassword);
    }

    public int deleteRoom(final RoomDeletionRequestDto dto) {
        final int roomId = dto.getRoomId();
        checkRoomExist(roomId);

        final RoomStatusDto roomStatusDto = roomDao.findStatusById(roomId);
        final String password = roomDao.findPasswordById(roomId);
        final boolean matchPassword = BCrypt.checkpw(dto.getPassword(), password);
        if (!matchPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (!roomStatusDto.getGameStatus().isEnd()) {
            throw new IllegalArgumentException("게임이 진행 중입니다.");
        }
        return roomDao.deleteById(roomId);
    }

    public CurrentTurnDto findCurrentTurn(final int roomId) {
        checkRoomExist(roomId);
        return roomDao.findCurrentTurnById(roomId);
    }

    private void checkRoomExist(final int roomId) {
        if (!roomDao.isExistId(roomId)) {
            throw new IllegalArgumentException("존재하지 않는 방 입니다.");
        }
    }
}
