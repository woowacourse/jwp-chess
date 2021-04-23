package chess.service.dao;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.board.position.Position;
import chess.domain.piece.Owner;
import chess.domain.piece.Rook;
import chess.service.RoomService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class GameDaoTest {

    @Autowired
    RoomService roomService;

    @Autowired
    GameDao gameDao;

    @Autowired
    ObjectMapper mapper;

    @Test
    void boardToData() {
    }

    @DisplayName("Json을 사용하여 board 저장 유효 테스트 / 객체 -> JSon 변환")
    @Test
    void saveBoardToDataWithJSon() throws IOException {
        final long id = roomService.save("tempRoom2");
        final ChessGame chessGame = gameDao.load(id);
        final Board board = chessGame.board();
        final String dataAsJson = gameDao.boardToData(board);
        final Map<String, String> stringTypeMap = mapper.readValue(dataAsJson, new TypeReference<Map<String, String>>() {});
        assertThat(stringTypeMap.get("a1")).isEqualTo("&#9814;");
    }

    @DisplayName("Json을 사용하여 board 불러오기 유효 테스트 / JSon -> 객체 변환")
    @Test
    void loadBoardToDataWithJSon() {
        final long id = roomService.save("tempRoom2");
        final ChessGame chessGame = gameDao.load(id);
        final String dataAsJson = gameDao.boardToData(chessGame.board());
        final Board board= gameDao.dataToBoard(dataAsJson);
        assertThat(board.of(new Position("a1"))).isEqualTo(Rook.of(Owner.WHITE));
    }
}