package chess.service;

import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import chess.dto.response.CurrentTurnDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomStatusDto;
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
        final boolean existName = roomDao.isExistName(dto.getRoomName());
        if (existName) {
            throw new IllegalArgumentException("이름이 같은 방이 이미 존재합니다.");
        }
        final String hashPassword = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        return roomDao.save(dto.getRoomName(), GameStatus.READY, Color.WHITE, hashPassword);
    }

    public void startGame(final int roomId) {
        final RoomStatusDto statusDto = roomDao.findStatusById(roomId);
        final GameStatus gameStatus = statusDto.getGameStatus();
        gameStatus.checkReady();
        roomDao.updateStatusById(roomId, GameStatus.PLAYING);
    }

    public void deleteRoom(final RoomDeletionRequestDto dto) {
        final int roomId = dto.getRoomId();

        checkRoomExist(roomId);
        checkPassword(dto.getPassword(), roomId);
        checkRoomStatus(roomId);

        final int deletedRow = roomDao.deleteById(roomId);
        if (deletedRow != 1) {
            throw new IllegalArgumentException("방을 삭제할 수 없습니다.");
        }
    }

    private void checkRoomExist(final int roomId) {
        if (!roomDao.isExistId(roomId)) {
            throw new IllegalArgumentException("존재하지 않는 방 입니다.");
        }
    }

    private void checkPassword(final String plainPassword, final int roomId) {
        if (plainPassword.isBlank()) {
            throw new IllegalArgumentException("요청에 비밀번호가 존재하지 않습니다.");
        }
        final String password = roomDao.findPasswordById(roomId);
        final boolean matchPassword = BCrypt.checkpw(plainPassword, password);
        if (!matchPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    private void checkRoomStatus(final int roomId) {
        final RoomStatusDto roomStatusDto = roomDao.findStatusById(roomId);
        if (!roomStatusDto.getGameStatus().isEnd()) {
            throw new IllegalArgumentException("게임이 진행 중입니다.");
        }
    }

    public CurrentTurnDto findCurrentTurn(final int roomId) {
        return roomDao.findCurrentTurnById(roomId);
    }
}
