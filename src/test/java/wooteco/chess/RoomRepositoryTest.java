package wooteco.chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import wooteco.chess.entity.BoardEntity;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.entity.TurnEntity;
import wooteco.chess.repository.RoomRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJdbcTest
@Transactional
public class RoomRepositoryTest {
    @Autowired
    private RoomRepository roomRepository;

    @DisplayName("새로운 게임방이 잘 생성되었는지 확인")
    @ParameterizedTest
    @ValueSource(strings = {"1번방", "#2", "드루와드루와", "go..chess..game.."})
    void createNewRoom(String name) {
        RoomEntity roomEntity = roomRepository.save(new RoomEntity(name));

        assertThat(roomEntity.getId()).isNotNull();
        assertThat(roomEntity.getName()).isEqualTo(name);
    }

    @DisplayName("새로 만든 게임방을 잘 찾는지 확인")
    @ParameterizedTest
    @ValueSource(strings = {"1번방", "#2", "드루와드루와", "go..chess..game.."})
    void findRoomById(String name) {
        RoomEntity roomEntity = roomRepository.save(new RoomEntity(name));
        RoomEntity targetRoomEntity =
                roomRepository.findById(roomEntity.getId()).orElseThrow(IllegalArgumentException::new);
        assertThat(targetRoomEntity.getName()).isEqualTo(name);
    }

    @DisplayName("게임방들을 모두 잘 저장하고 있는지 확인")
    @Test
    void findAllRooms() {
        roomRepository.save(new RoomEntity("1번방"));
        roomRepository.save(new RoomEntity("#2"));
        roomRepository.save(new RoomEntity("드루와드루와"));
        roomRepository.save(new RoomEntity("go..chess..game.."));

        List<RoomEntity> rooms = roomRepository.findAll();
        assertThat(rooms.size()).isEqualTo(4);
    }

    @DisplayName("새로 만든 게임방에는 체스게임 정보가 없는 것이 맞는지 확인")
    @Test
    void validateChessGameNotSaved() {
        RoomEntity roomEntity = roomRepository.save(new RoomEntity("테스트"));

        assertThat(roomEntity.hasEmptyBoard()).isTrue();
        assertThat(roomEntity.hasEmptyTurn()).isTrue();
    }

    @DisplayName("새로 만든 게임방에 체스게임 정보가 잘 저장되어 있는지 확인")
    @Test
    void validateChessGameSaved() {
        RoomEntity newRoomEntity = new RoomEntity("테스트");
        newRoomEntity.addBoard(new BoardEntity("a1", "p"));
        newRoomEntity.addBoard(new BoardEntity("b2", "K"));
        newRoomEntity.addBoard(new BoardEntity("c3", "Q"));
        newRoomEntity.addBoard(new BoardEntity("d4", "n"));
        TurnEntity turnEntity = new TurnEntity("White");

        RoomEntity savedRoomEntity = roomRepository.save(new RoomEntity(newRoomEntity, turnEntity));
        RoomEntity loadedRoomEntity =
                roomRepository.findById(savedRoomEntity.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(loadedRoomEntity.getId()).isNotNull();
        assertThat(loadedRoomEntity.getName()).isEqualTo("테스트");
        assertThat(loadedRoomEntity.hasEmptyBoard()).isFalse();
        assertThat(loadedRoomEntity.hasEmptyTurn()).isFalse();
        assertThat(loadedRoomEntity.getTurnEntity().getTeamName()).isEqualTo("White");
        assertThat(loadedRoomEntity.getBoardEntities().size()).isEqualTo(4);
    }

    @DisplayName("게임방에 저장한 체스말 정보만 잘 삭제하는지 확인")
    @Test
    void validateBoardDataDeleted() {
        RoomEntity newRoomEntity = new RoomEntity("테스트");
        newRoomEntity.addBoard(new BoardEntity("a1", "p"));
        newRoomEntity.addBoard(new BoardEntity("b2", "K"));
        newRoomEntity.addBoard(new BoardEntity("c3", "Q"));
        newRoomEntity.addBoard(new BoardEntity("d4", "n"));
        TurnEntity turnEntity = new TurnEntity("White");

        RoomEntity savedRoomEntity = roomRepository.save(new RoomEntity(newRoomEntity, turnEntity));
        savedRoomEntity.deleteAllBoard();

        assertThat(savedRoomEntity.hasEmptyBoard()).isTrue();
    }

    @DisplayName("게임방에 저장한 체스게임 정보를 모두 잘 삭제하는지 확인")
    @Test
    void validateChessGameDeleted() {
        RoomEntity newRoomEntity = new RoomEntity("테스트");
        newRoomEntity.addBoard(new BoardEntity("a1", "p"));
        newRoomEntity.addBoard(new BoardEntity("b2", "K"));
        newRoomEntity.addBoard(new BoardEntity("c3", "Q"));
        newRoomEntity.addBoard(new BoardEntity("d4", "n"));
        TurnEntity turnEntity = new TurnEntity("White");

        RoomEntity savedRoomEntity = roomRepository.save(new RoomEntity(newRoomEntity, turnEntity));
        roomRepository.deleteById(savedRoomEntity.getId());

        assertThatThrownBy(() -> {
            roomRepository.findById(savedRoomEntity.getId()).orElseThrow(IllegalArgumentException::new);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
