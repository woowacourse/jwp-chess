package chess.domain.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SparkHistoryDaoTest {
    private SparkHistoryDao sparkHistoryDao;

    @BeforeEach
    void setUp() {
        sparkHistoryDao = new SparkHistoryDao();
    }

    @Test
    void insert() throws SQLException {
        sparkHistoryDao.insert("minjeong");
    }

    @Test
    void name() throws SQLException {
        sparkHistoryDao.insert("minjeong");
        final Optional<Integer> id = sparkHistoryDao.findIdByName("minjeong");
        System.out.println(id.get());
    }

    @Test
    void delete() throws SQLException {
        sparkHistoryDao.insert("minjeong");
        sparkHistoryDao.delete("minjeong");
    }

    @Test
    void selectAll() throws SQLException {
        sparkHistoryDao.insert("minjeong");
        sparkHistoryDao.insert("joanne");
        final List<String> names = sparkHistoryDao.selectActive();
        assertThat(names).contains("minjeong", "joanne");
    }
}