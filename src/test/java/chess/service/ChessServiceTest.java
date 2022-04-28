package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.PieceDao;
import chess.dao.RoomDao;
import chess.domain.GameStatus;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomDeleteRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.ScoreResponseDto;
import chess.exception.IllegalCommandException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class ChessServiceTest {

    @Autowired
    private ChessService chessService;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private PieceDao pieceDao;

    @Test
    @DisplayName("방을 생성하고 기물들을 초기화한다.")
    void createRoom() {
        final RoomResponseDto roomResponseDto = createTestRoom("다 드루와");

        assertAll(
                () -> assertThat(roomResponseDto.getName()).isEqualTo("다 드루와"),
                () -> assertThat(chessService.loadAllPiece(roomResponseDto.getId())).hasSize(32)
        );
    }

    @Test
    @DisplayName("모든 방 정보를 불러온다.")
    void loadAllRoom() {
        createTestRoom("다 드루와");
        createTestRoom("내가 이긴다");

        assertThat(chessService.loadAllRoom().size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("방 정보를 불러온다.")
    void loadRoom() {
        final long id = createTestRoom("다 드루와").getId();

        assertThat(chessService.loadRoom(id).getName()).isEqualTo("다 드루와");
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        final Long id = createTestRoom("다 드루와").getId();
        chessService.end(id);

        final int originSize = chessService.loadAllRoom().size();

        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("1234");
        chessService.deleteRoom(id, roomDeleteRequestDto);

        assertThat(chessService.loadAllRoom().size()).isLessThan(originSize);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않는 경우 방을 삭제할 수 없다.")
    void deleteRoomException1() {
        final Long id = createTestRoom("다 드루와").getId();
        chessService.end(id);

        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("123");

        assertThatThrownBy(() -> chessService.deleteRoom(id, roomDeleteRequestDto))
                .isInstanceOf(IllegalCommandException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("게임이 진행 중인 경우 방을 삭제할 수 없다.")
    void deleteRoomException2() {
        final Long id = createTestRoom("다 드루와").getId();

        final RoomDeleteRequestDto roomDeleteRequestDto = new RoomDeleteRequestDto("1234");

        assertThatThrownBy(() -> chessService.deleteRoom(id, roomDeleteRequestDto))
                .isInstanceOf(IllegalCommandException.class)
                .hasMessage("게임이 진행중인 방은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("방의 모든 기물 정보를 불러온다.")
    void loadAllPiece() {
        final Long id = createTestRoom("다 드루와").getId();

        assertThat(chessService.loadAllPiece(id)).hasSize(32);
    }

    @Test
    @DisplayName("기물을 움직인다.")
    void movePiece() {
        final Long id = createTestRoom("다 드루와").getId();
        final MoveRequestDto moveRequestDto = new MoveRequestDto("b2", "b4");

        chessService.movePiece(id, moveRequestDto);

        assertThat(pieceDao.findByRoomIdAndPosition(id, "b4").getType()).isEqualTo("pawn");
    }

    @Test
    @DisplayName("현재 정수를 불러온다.")
    void loadScore() {
        final Long id = createTestRoom("다 드루와").getId();

        final ScoreResponseDto scoreResponseDto = chessService.loadScore(id);

        assertAll(
                () -> assertThat(scoreResponseDto.getWhiteScore()).isEqualTo(38),
                () -> assertThat(scoreResponseDto.getBlackScore()).isEqualTo(38)
        );
    }

    @Test
    @DisplayName("게임을 종료한다.")
    void end() {
        final Long id = createTestRoom("다 드루와").getId();

        chessService.end(id);

        assertThat(roomDao.findById(id).getStatus()).isEqualTo(GameStatus.END.getValue());
    }

    private RoomResponseDto createTestRoom(final String name) {
        final RoomRequestDto roomRequestDto = new RoomRequestDto(name, "1234");
        return chessService.createRoom(roomRequestDto);
    }
}
