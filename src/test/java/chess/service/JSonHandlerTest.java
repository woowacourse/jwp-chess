package chess.service;

import chess.domain.ChessGame;
import chess.domain.piece.Owner;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceSymbolMapper;
import chess.service.dao.GameDao;
import chess.util.JSonHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Transactional
@SpringBootTest
class JSonHandlerTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private JSonHandler jSonHandler;

    @Autowired
    private ObjectMapper mapper;

    private ChessGame chessGame;

    @BeforeEach
    private void initGame() {
        final long id = roomService.create("tempRoom", "test");
        chessGame = gameDao.load(id);
    }

    @DisplayName("Json을 사용하여 board 불러오기 유효 테스트 / JSon -> board map 변환")
    @Test
    public void jsonDataToStringMap() {
        try {
            final String data = jSonHandler.mapToJsonData(chessGame.board().parseUnicodeBoardAsMap());
            final Map<String, String> stringMap = mapper.readValue(data, new TypeReference<Map<String, String>>() {
            });
            final Piece piece = PieceSymbolMapper.parseToPiece(stringMap.get("a2"));
            assertThat(piece).isEqualTo(Pawn.of(Owner.WHITE));
        } catch (Exception e) {
            fail("json 바인딩 에러");
        }
    }

    @DisplayName("Json을 사용하여 board 저장 유효 테스트 / 객체 -> JSon 변환")
    @Test
    public void saveBoardToDataWithJSon() {
        try {
            final String data = mapper.writeValueAsString(chessGame.board().parseUnicodeBoardAsMap());
            final Map<String, String> stringMap = jSonHandler.jsonDataToStringMap(data);
            final Piece piece = PieceSymbolMapper.parseToPiece(stringMap.get("a2"));
            assertThat(piece).isEqualTo(Pawn.of(Owner.WHITE));
        } catch (Exception e) {
            fail("json 바인딩 에러");
        }
    }
}