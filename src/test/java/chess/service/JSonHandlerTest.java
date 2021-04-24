package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.piece.*;
import chess.service.dao.GameDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class JSonHandlerTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private JSonHandler jSonHandler;

    @DisplayName("Json을 사용하여 board 불러오기 유효 테스트 / JSon -> board map 변환")
    @Test
    public void jsonDataToStringMap() {
        final long id = roomService.save("tempRoom2", "test");
        final ChessGame chessGame = gameDao.load(id);

        final String data = jSonHandler.mapToJsonData(chessGame.board().parseUnicodeBoardAsMap());
        final Map<String, String> stringMap = jSonHandler.jsonDataToStringMap(data);

        final Piece piece = PieceSymbolMapper.parseToPiece(stringMap.get("a2"));
        assertThat(piece).isEqualTo(Pawn.of(Owner.WHITE));
    }

    @DisplayName("Json을 사용하여 board 저장 유효 테스트 / 객체 -> JSon 변환")
    @Test
    public void saveBoardToDataWithJSon() {
        final long id = roomService.save("tempRoom2", "test");
        final ChessGame chessGame = gameDao.load(id);

        final Board board = chessGame.board();
        final String data = jSonHandler.mapToJsonData(board.parseUnicodeBoardAsMap());

        final Map<String, String> stringMap = jSonHandler.jsonDataToStringMap(data);
        final Piece piece = PieceSymbolMapper.parseToPiece(stringMap.get("a2"));
        assertThat(piece).isEqualTo(Pawn.of(Owner.WHITE));
    }
}