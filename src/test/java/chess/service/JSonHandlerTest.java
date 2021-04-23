package chess.service;

import chess.domain.ChessGame;
import chess.domain.board.Board;
import chess.domain.piece.Owner;
import chess.domain.piece.PieceSymbolMapper;
import chess.domain.piece.Rook;
import chess.service.dao.GameDao;
import org.graalvm.compiler.graph.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JSonHandlerTest {

    @Autowired
    RoomService roomService;

    @Autowired
    GameDao gameDao;

    @Autowired
    JSonHandler jSonHandler;

    @Test
    void mapToJsonData() {
    }

    @Test
    void jsonDataToStringMap() {
        final long id = roomService.save("tempRoom2");
        final ChessGame chessGame = gameDao.load(id);

        final String data = jSonHandler.mapToJsonData(chessGame.board().parseUnicodeBoardAsMap());
        final Map<String, String> stringMap = jSonHandler.jsonDataToStringMap(data);

        final String dataAsJson = gameDao.boardToData(chessGame.board());
        final Board board= gameDao.dataToBoard(dataAsJson);
        final Position position = PieceSymbolMapper.parseToPiece(stringMap.get("a1");
        assertThat(.isEqualTo(Rook.of(Owner.WHITE));
    }
}