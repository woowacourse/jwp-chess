package wooteco.chess;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.repository.RoomRepository;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class RoomRepositoryTest {
	@Autowired
	private RoomRepository roomRepository;

	@DisplayName("방 생성 후 보드 추가.")
	@Test
	void name() {
		RoomEntity roomEntity = roomRepository.save(new RoomEntity("재밌는 체스!"));
		roomEntity.addBoard(new BoardEntity("A2", "P"));
		roomEntity.addBoard(new BoardEntity("A3", "P"));

		RoomEntity updateRoom = roomRepository.save(roomEntity);

		assertThat(updateRoom.getId()).isNotNull();
		assertThat(updateRoom.getBoardEntities()).hasSize(2);
	}

	@DisplayName("방 생성 후 전체 삭제 테스트")
	@Test
	void name2() {
		RoomEntity roomEntity = roomRepository.save(new RoomEntity("재밌는 체스!"));
		roomEntity.addBoard(new BoardEntity("A2", "P"));
		roomEntity.addBoard(new BoardEntity("A3", "P"));
		roomEntity.deleteAllBoard();

		RoomEntity updateRoom = roomRepository.save(roomEntity);

		assertThat(updateRoom.getId()).isNotNull();
		assertThat(updateRoom.getBoardEntities()).hasSize(0);
	}
}