package wooteco.chess.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.entity.GameEntity;
import wooteco.chess.entity.Room;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
class RoomRepositoryTest {

    @Autowired
    RoomRepository roomRepository;

    @DisplayName("새로운 방 저장")
    @Test
    public void save() {
        Room room = new Room("lowoon");

        roomRepository.save(room);

        assertThat(room.getId()).isNotNull();
    }

    @DisplayName("새로운 방 저장시 저장된 board 확인")
    @Test
    public void saveBoard() {
        GameEntity gameEntity = new GameEntity(0);
        gameEntity.addBoard(BoardFactory.createInitialBoard());
        Room room = new Room("lowoon", gameEntity);

        roomRepository.save(room);
        room = roomRepository.findByName("lowoon").orElseThrow(AssertionError::new);
        gameEntity = room.getGame();
        Board board = gameEntity.createBoard();

        assertThat(board).isEqualTo(BoardFactory.createInitialBoard());
    }
}