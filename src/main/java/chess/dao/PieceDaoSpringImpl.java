package chess.dao;

import chess.domain.position.Position;
import chess.dto.PieceDto;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class PieceDaoSpringImpl implements PieceDao {

    private JdbcTemplate jdbcTemplate;

    public PieceDaoSpringImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void remove(Position position) {

    }

    @Override
    public void removeAll() {

    }

    @Override
    public void saveAll(List<PieceDto> pieceDtos) {

    }

    @Override
    public void save(PieceDto pieceDto) {
        final String sql = "insert into piece (position, type, color) values (?, ?, ?)";
        jdbcTemplate.update(sql, pieceDto.getPosition(), pieceDto.getType(), pieceDto.getColor());
    }

    @Override
    public List<PieceDto> findAll() {
        return null;
    }

    @Override
    public void update(Position source, Position target) {

    }
}
