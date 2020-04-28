package wooteco.chess.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.repository.PieceEntity;
import wooteco.chess.repository.RoomEntity;
import wooteco.chess.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @DisplayName("방 저장 확인")
    @Test
    void addRoom() {
        RoomEntity roomEntity = new RoomEntity("방1", Team.WHITE);
        RoomEntity savedRoomEntity = roomRepository.save(roomEntity);
        assertThat(savedRoomEntity).isNotNull();
    }

    @DisplayName("방 이름을 입력하면 방 번호가 반환되는 지 확인")
    @Test
    void findIdByName() {
        RoomEntity roomEntity = new RoomEntity("방1", Team.WHITE);
        roomRepository.save(roomEntity);
        assertThat(roomRepository.findIdByName("방1")).isNotNull();
    }

    @DisplayName("save 테스트")
    @Test
    void save() {
        Set<PieceEntity> pieceEntities = new HashSet<>();
        for (Piece alivePiece : new Board().getPieces().getAlivePieces()) {
            pieceEntities.add(new PieceEntity(alivePiece.getPosition(), alivePiece.toString(), alivePiece.getTeam()));
        }
        assertThat(roomRepository.save(new RoomEntity(1L, "방1", Team.WHITE, pieceEntities))).isNotNull();
    }
}
