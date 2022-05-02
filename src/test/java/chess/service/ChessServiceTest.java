package chess.service;

import static org.assertj.core.api.Assertions.*;

import chess.dao.FakeGameDao;
import chess.dao.FakePieceDao;
import chess.dao.FakeRoomDao;
import chess.domain.piece.Color;
import chess.domain.state.Result;
import chess.dto.BoardElementDto;
import chess.dto.ChessGameDto;
import chess.dto.DeleteRequestDto;
import chess.dto.DeleteResponseDto;
import chess.dto.PieceDto;
import chess.dto.RoomDto;
import chess.repository.GameRepository;
import chess.repository.PieceRepository;
import chess.repository.RoomRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChessServiceTest {
    private ChessService chessService;
    private GameRepository gameRepository;
    private PieceRepository pieceRepository;
    private RoomRepository roomRepository;

    @BeforeEach
    void setUp() {
        gameRepository = new GameRepository(new FakeGameDao());
        pieceRepository = new PieceRepository(new FakePieceDao());
        roomRepository = new RoomRepository(new FakeRoomDao());
        chessService = new ChessService(gameRepository, roomRepository, pieceRepository);
        chessService.createRoom("chess", "0000");
    }

    @AfterEach
    void clean() {
        chessService.deleteGame(new DeleteRequestDto(1, "0000"));
    }

    @Test
    void createRoomTest() {
        Color color = gameRepository.getColorFromStorage(1);
        List<BoardElementDto> boardElementDtos = pieceRepository.findAll(1);
        List<RoomDto> roomDtos = roomRepository.findAll();
        assertThat(color.isWhite() && boardElementDtos.size() == 32 && roomDtos.size() == 1).isTrue();
    }

    @Test
    void newGameTest() {
        chessService.newGame(1);
        List<BoardElementDto> boardElementDtos = pieceRepository.findAll(1);
        Color color = gameRepository.getColorFromStorage(1);
        String status = roomRepository.findAll().get(0).getStatus();
        assertThat(color.isWhite() && boardElementDtos.size() == 32 && status.equals("PLAY")).isTrue();
    }

    @Test
    void loadGameTest() {
        ChessGameDto chessGameDto = chessService.loadGame(1);
        assertThat(chessGameDto.getPositionsAndPieces().size() == 32
        && chessGameDto.getWhiteScore().get(Color.WHITE) == 38.0
        && chessGameDto.getBlackScore().get(Color.BLACK) == 38.0
        && chessGameDto.getResult().equals(Result.EMPTY)
        ).isTrue();
    }

    @Test
    void moveTest() {
        ChessGameDto chessGameDto = chessService.move(1, "A2", "A4");
        Map<String, PieceDto> positionsAndPieces = chessGameDto.getPositionsAndPieces();
        Set<String> positions = positionsAndPieces.keySet();
        Color turn = gameRepository.getColorFromStorage(1);
        assertThat(positions.size() == 32 && positions.contains("A4") && !turn.isWhite()).isTrue();
    }

    @Test
    void endGameTest() {
        chessService.endGame(1);
        String status = roomRepository.findAll().get(0).getStatus();
        assertThat(status).isEqualTo("STOP");
    }

    @Test
    void deleteGameTest() {
        chessService.createRoom("chess2", "0000");
        DeleteResponseDto deleteResponseDto = chessService.deleteGame(new DeleteRequestDto(2, "0000"));
        assertThat(deleteResponseDto.getIsDeleted()).isTrue();
    }
}
