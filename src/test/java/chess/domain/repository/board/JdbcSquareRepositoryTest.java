package chess.domain.repository.board;

import chess.domain.board.BoardInitializer;
import chess.domain.board.piece.EmptyPiece;
import chess.domain.board.piece.Owner;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.Square;
import chess.domain.board.piece.king.King;
import chess.domain.board.position.Position;
import chess.domain.manager.Game;
import chess.domain.repository.game.GameRepository;
import chess.domain.repository.game.JdbcGameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JdbcSquareRepositoryTest {

    private final GameRepository gameRepository;
    private final SquareRepository squareRepository;

    public JdbcSquareRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.gameRepository = new JdbcGameRepository(jdbcTemplate);
        this.squareRepository = new JdbcSquareRepository(jdbcTemplate);
    }

    @Test
    void save() {
        //given
        Long gameId = gameRepository.save(new Game());
        Square square = new Square(gameId, Position.of("a2"), EmptyPiece.getInstance());

        //when
        Long squareId = squareRepository.save(square);

        //then
        assertThat(squareId).isNotNull();
    }

    @Test
    void saveBoard() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);

        //when
        squareRepository.saveBoard(gameId, BoardInitializer.initiateBoard());
        List<Square> squares = squareRepository.findByGameId(gameId);

        //then
        assertThat(squares).hasSize(64);
    }

    @Test
    void findById() {
        //given
        Game game = new Game();
        Long gameId = gameRepository.save(game);
        Position expectedPosition = Position.of("a2");
        Piece expectedPiece = EmptyPiece.getInstance();
        Square square = new Square(gameId, expectedPosition, expectedPiece);

        //when
        Long squareId = squareRepository.save(square);
        Square findSquare = squareRepository.findById(squareId);
        Position resultPosition = findSquare.getPosition();
        Piece resultPiece = findSquare.getPiece();

        //then
        assertThat(expectedPiece).isEqualTo(resultPiece);
        assertThat(expectedPosition).isEqualTo(resultPosition);
    }

    @Test
    void findByGameId() {
        //given
        Long gameId = gameRepository.save(new Game());
        Position expectedPosition = Position.of("a2");
        Piece expectedPiece = EmptyPiece.getInstance();
        Square square = new Square(gameId, expectedPosition, expectedPiece);
        squareRepository.save(square);

        //when
        // squareRepository.save(square);
        List<Square> squares = squareRepository.findByGameId(gameId);

        //then
        assertThat(squares).hasSize(1);
    }

    @Test
    void updateByGameIdAndPosition() {
        //given
        Long gameId = gameRepository.save(new Game());
        Position expectedPosition = Position.of("a2");
        Piece expectedPiece = EmptyPiece.getInstance();
        Square square = new Square(gameId, expectedPosition, expectedPiece);
        Long squareId = squareRepository.save(square);

        //when
        Piece updatePiece = King.getInstanceOf(Owner.BLACK);
        Square updateSquare = new Square(squareId, gameId, expectedPosition, updatePiece);
        squareRepository.updateByGameIdAndPosition(updateSquare);
        Square resultSquare = squareRepository.findById(squareId);

        //then
        assertThat(updatePiece).isEqualTo(resultSquare.getPiece());
        assertThat(expectedPosition).isEqualTo(resultSquare.getPosition());
    }
}