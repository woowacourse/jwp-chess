package chess.service;

import chess.domain.piece.Piece;
import chess.domain.player.Round;
import chess.domain.position.Position;
import chess.repository.ChessRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ChessServiceTest {
    @InjectMocks
    private ChessService chessService;

    @Mock
    private ChessRepository chessRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("방 생성 테스트")
    @Test
    void makeRoomTest() {
        Round round = new Round();
        Map<String, String> board = filteredChessBoard(round.getBoard());

        given(chessRepository.makeRoom(board, "room4"))
                .willReturn(4);

        chessService.makeRoom("room4");
        verify(chessRepository, times(1)).makeRoom(board, "room4");
    }

    private Map<String, String> filteredChessBoard(final Map<Position, Piece> chessBoard) {
        Map<String, String> filteredChessBoard = new LinkedHashMap<>();
        for (Map.Entry<Position, Piece> cell : chessBoard.entrySet()) {
            filter(filteredChessBoard, cell);
        }
        return filteredChessBoard;
    }

    private void filter(Map<String, String> filteredChessBoard, Map.Entry<Position, Piece> cell) {
        if (isPieceExist(cell)) {
            filteredChessBoard.put(cell.getKey().toString(), cell.getValue().getPiece());
        }
    }

    private boolean isPieceExist(Map.Entry<Position, Piece> chessBoardEntry) {
        return chessBoardEntry.getValue() != null;
    }
}
