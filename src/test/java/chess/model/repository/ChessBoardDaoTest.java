package chess.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.domain.board.ChessGame;
import chess.model.domain.board.Square;
import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Team;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChessBoardDaoTest {

    private static final ChessBoardDao CHESS_BOARD_DAO = ChessBoardDao.getInstance();
    private static final ChessGameDao CHESS_GAME_DAO = ChessGameDao.getInstance();
    private static final RoomDao ROOM_DAO = RoomDao.getInstance();
    private static final int ROOM_ID;
    private static final int GAME_ID;
    private static final Map<Square, Boolean> CASTLING_ELEMENTS;
    private static final TeamScore TEAM_SCORE = new ChessGame().deriveTeamScore();

    static {
        Map<Square, Boolean> castlingElements = new ChessGame().getChessBoard().keySet()
            .stream()
            .collect(Collectors.toMap(boardSquare -> boardSquare, boardSquare -> false));
        castlingElements.put(Square.of("a1"), true);
        CASTLING_ELEMENTS = Collections.unmodifiableMap(castlingElements);

        ROOM_ID = ROOM_DAO.create("테스트방", "");
        Map<Team, String> userNames = new HashMap<>();
        userNames.put(Team.BLACK, "BLACK");
        userNames.put(Team.WHITE, "WHITE");
        GAME_ID = CHESS_GAME_DAO.create(ROOM_ID, Team.BLACK, userNames, TEAM_SCORE);
    }

    @AfterAll
    static void tearDownAll() {
        CHESS_GAME_DAO.updateProceedN(GAME_ID);
        ROOM_DAO.updateUsedN(ROOM_ID);
    }

    @BeforeEach
    void setUp() {
        ChessGame chessGame = new ChessGame();
        CHESS_BOARD_DAO.create(GAME_ID, chessGame.getChessBoard(), CASTLING_ELEMENTS,
            chessGame.getEnPassants());
    }

    @AfterEach
    void tearDown() {
        CHESS_BOARD_DAO.delete(GAME_ID);
    }

    @Test
    void getInstance() {
        ChessBoardDao chessBoardDao1 = ChessBoardDao.getInstance();
        ChessBoardDao chessBoardDao2 = ChessBoardDao.getInstance();
        assertThat(chessBoardDao1 == chessBoardDao2).isTrue();
    }

    @Test
    void insert() {
        CHESS_BOARD_DAO.delete(GAME_ID);
        assertThat(CHESS_BOARD_DAO.findBoard(GAME_ID)).isEmpty();
        assertThat(CHESS_BOARD_DAO.findCastlingElements(GAME_ID)).isEmpty();
        assertThat(CHESS_BOARD_DAO.findEnpassantBoard(GAME_ID).getEnPassantsKeys()).isEmpty();

        CHESS_BOARD_DAO.create(GAME_ID, new ChessGame().getChessBoard(), CASTLING_ELEMENTS,
            new ChessGame().getEnPassants());
        assertThat(CHESS_BOARD_DAO.findBoard(GAME_ID)).isEqualTo(new ChessGame().getChessBoard());
        assertThat(CHESS_BOARD_DAO.findCastlingElements(GAME_ID).size()).isEqualTo(1);
        assertThat(CHESS_BOARD_DAO.findEnpassantBoard(GAME_ID).getEnPassantsKeys()).isEmpty();
    }

    @Test
    void getCastlingElements() {
        assertThat(CHESS_BOARD_DAO.findCastlingElements(GAME_ID).size()).isEqualTo(1);

        CHESS_BOARD_DAO.delete(GAME_ID);
        assertThat(CHESS_BOARD_DAO.findCastlingElements(GAME_ID)).isEmpty();
    }

    @Test
    void getEnpassantBoard() {
        assertThat(CHESS_BOARD_DAO.findEnpassantBoard(GAME_ID).getEnPassantsKeys()).isEmpty();
    }

    @Test
    void getBoard() {
        assertThat(CHESS_BOARD_DAO.findBoard(GAME_ID)).isEqualTo(new ChessGame().getChessBoard());

        CHESS_BOARD_DAO.delete(GAME_ID);
        assertThat(CHESS_BOARD_DAO.findBoard(GAME_ID)).isEmpty();
    }

    @Test
    void delete() {
        CHESS_BOARD_DAO.delete(GAME_ID);
        assertThat(CHESS_BOARD_DAO.findBoard(GAME_ID)).isEmpty();
        assertThat(CHESS_BOARD_DAO.findCastlingElements(GAME_ID)).isEmpty();
        assertThat(CHESS_BOARD_DAO.findEnpassantBoard(GAME_ID).getEnPassantsKeys()).isEmpty();
    }
}