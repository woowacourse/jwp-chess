package chess.database.dao;

import chess.domain.board.ChessBoard;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.gamestate.Running;
import chess.dto.RoomDto;
import chess.dto.SaveRoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@TestPropertySource(locations = "classpath:application-test.properties")
class SpringRoomDaoTest {

    private SpringRoomDao springRoomDao;
    private ChessGame chessGame;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springRoomDao = new SpringRoomDao(jdbcTemplate);
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.initBoard();
        chessGame = new ChessGame(chessBoard, Color.WHITE, new Running());
    }

    @Test
    @DisplayName("방 생성 확인 테스트")
    void createRoom() {
        //given
        SaveRoomDto saveRoomDto = new SaveRoomDto("test", chessGame);

        //when
        int roomNo = springRoomDao.addRoom(saveRoomDto);
        RoomDto roomDto = springRoomDao.findRoomByRoomNo(roomNo);

        //then
        assertThat(roomDto).isEqualTo(new RoomDto(roomNo, saveRoomDto.getRoomName(), saveRoomDto.getTurn(), saveRoomDto.getChessBoard()));
    }

    @Test
    @DisplayName("방 수정 확인 테스트")
    void updateRoom() {
        //given
        SaveRoomDto saveRoomDto = new SaveRoomDto("test", chessGame);
        int roomNo = springRoomDao.addRoom(saveRoomDto);
        chessGame.play(Arrays.asList("move b2 b4".split(" ")));
        RoomDto roomDto = new RoomDto(roomNo, "test", chessGame);

        //when
        springRoomDao.updateRoom(roomDto);

        //then
        assertThat(roomDto).isEqualTo(springRoomDao.findRoomByRoomNo(roomNo));
    }

    @Test
    @DisplayName("방 삭제 확인 테스트")
    void deleteRoom() {
        //given
        SaveRoomDto saveRoomDto = new SaveRoomDto("test", chessGame);
        int roomNo = springRoomDao.addRoom(saveRoomDto);

        //when
        int result = springRoomDao.deleteRoomByRoomNo(roomNo);

        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("방 목록 확인 테스트")
    void getAllRooms() {
        //given
        SaveRoomDto saveRoomDto1 = new SaveRoomDto("test1", chessGame);
        SaveRoomDto saveRoomDto2 = new SaveRoomDto("test2", chessGame);

        //when
        int roomNo1 = springRoomDao.addRoom(saveRoomDto1);
        int roomNo2 = springRoomDao.addRoom(saveRoomDto2);
        List<RoomDto> rooms = springRoomDao.getAllRoom();

        //then
        assertThat(rooms).isEqualTo(Arrays.asList(
                new RoomDto(roomNo1, saveRoomDto1.getRoomName(), saveRoomDto1.getTurn(), saveRoomDto1.getChessBoard()),
                new RoomDto(roomNo2, saveRoomDto2.getRoomName(), saveRoomDto2.getTurn(), saveRoomDto2.getChessBoard())
                ));
    }
}