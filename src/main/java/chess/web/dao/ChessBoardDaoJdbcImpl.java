package chess.web.dao;

import chess.domain.piece.Piece;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.position.Position;
import chess.web.dto.ChessBoardDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ChessBoardDaoJdbcImpl implements ChessBoardDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ChessBoardDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ChessBoardDto> actorRowMapper = (resultSet, rowNum) -> {
        ChessBoardDto board = new ChessBoardDto(
                resultSet.getString("position"),
                resultSet.getString("piece")
        );
        return board;
    };

    @Override
    public void save(Position position, Piece piece) {
        final String sql = "insert into board (position, piece) values (?, ?)";
        this.jdbcTemplate.update(
                sql,
                position.toString(),
                piece.toString());
    }

    @Override
    public void saveById(int id, Position position, Piece piece) {
        final String sql = "insert into board (id, position, piece) values (?, ?, ?)";
        this.jdbcTemplate.update(
                sql,
                id,
                position.toString(),
                piece.toString());
    }

    @Override
    public void deleteAll() {
        final String sql = "delete from board";
        this.jdbcTemplate.update(sql);
    }

    @Override
    public void deleteById(int id) {
        final String sql = "delete from board where id = " + id;
        this.jdbcTemplate.update(sql);
    }

    @Override
    public Map<Position, Piece> findAll() {
        final String sql = "select position, piece from board";
        List<ChessBoardDto> chessBoardDtos = jdbcTemplate.query(sql, actorRowMapper);

        Map<Position, Piece> board = new HashMap<>();
        for (ChessBoardDto boardDto : chessBoardDtos) {
            String position = boardDto.getPosition();
            String piece = boardDto.getPiece();
            board.put(Position.of(position), PieceFactory.of(position, piece));
        }

        return board;
    }

    @Override
    public Map<Position, Piece> findById(int id) {
        final String sql = "select position, piece from board where id = " + id;
        List<ChessBoardDto> chessBoardDtos = jdbcTemplate.query(sql, actorRowMapper);

        Map<Position, Piece> board = new HashMap<>();
        for (ChessBoardDto boardDto : chessBoardDtos) {
            String position = boardDto.getPosition();
            String piece = boardDto.getPiece();
            board.put(Position.of(position), PieceFactory.of(position, piece));
        }

        return board;
    }
}
