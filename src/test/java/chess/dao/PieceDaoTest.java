package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.entity.PieceEntity;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class PieceDaoTest {
    private GameDao gameDao;
    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDbDao(jdbcTemplate);
        pieceDao = new PieceDbDao(jdbcTemplate);

        jdbcTemplate.execute("create table game("
                + "game_id int primary key auto_increment,"
                + "current_turn varchar(10) default'WHITE')");
        jdbcTemplate.update("insert into game(current_turn) values (?)","WHITE");

        jdbcTemplate.execute("create table piece("
                + "piece_id int primary key auto_increment,"
                + "game_id int not null,"
                + "piece_name varchar(10) not null,"
                + "piece_color varchar(10) not null,"
                + "position varchar(2) not null,"
                + "foreign key (game_id) references game(game_id))");
        pieceDao.insert(PieceEntity.of(1, "p", "WHITE", "a2"));
    }

    @AfterEach
    void clean() {
        pieceDao.deleteAll(PieceEntity.of(1));
        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("drop table game if exists");
    }

    @Test
    void findTest() {
        PieceEntity pieceEntity = pieceDao.find(PieceEntity.of(1, "a2"));
        assertThat(pieceEntity.getPieceName().equals("p") && pieceEntity.getPieceColor().equals("WHITE"))
                .isTrue();
    }

    @Test
    void findAllTest() {
        pieceDao.insert(PieceEntity.of(1, "p", "WHITE", "a3"));
        assertThat(pieceDao.findAll(PieceEntity.of(1)).size()).isEqualTo(2);
    }

    @Test
    void updateTest() {
        PieceEntity pieceEntity = pieceDao.find(PieceEntity.of(1, "a2"));
        pieceDao.update(PieceEntity.of(1, pieceEntity.getPieceName(), pieceEntity.getPieceColor(), "a2"),
                PieceEntity.of(1, pieceEntity.getPieceName(), pieceEntity.getPieceColor(), "a3"));
        List<PieceEntity> pieceEntities = pieceDao.findAll(PieceEntity.of(1));
        assertThat(pieceEntities.size() == 1
                && pieceEntities.get(0).getPosition().equals("a3")).isTrue();
    }

    @Test
    void deleteTest() {
        pieceDao.insert(PieceEntity.of(1, "p", "WHITE", "a3"));
        pieceDao.delete(PieceEntity.of(1, "a2"));
        List<PieceEntity> pieceEntities = pieceDao.findAll(PieceEntity.of(1));
        assertThat(pieceEntities.size() == 1
        && pieceEntities.get(0).getPosition().equals("a3")).isTrue();
    }
}
