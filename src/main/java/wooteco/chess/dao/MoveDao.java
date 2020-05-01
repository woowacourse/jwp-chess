package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import wooteco.chess.domain.Game;
import wooteco.chess.domain.board.Path;
import wooteco.chess.dto.MoveRequestDto;

@Component
public class MoveDao implements MySqlJdbcTemplateDao {

    public void addMove(final Game game, final Path path) throws SQLException {
        String query = "INSERT INTO move (game, start_position, end_position) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, game.getId());
            statement.setString(2, path.getStart());
            statement.setString(3, path.getEnd());
            statement.executeUpdate();
        }
    }

    public List<MoveRequestDto> getMoves(final Game game) throws SQLException {
        String query = "SELECT * FROM move WHERE game = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, game.getId());
            List<MoveRequestDto> moves = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            addMoves(moves, resultSet);
            return moves;
        }
    }

    private void addMoves(final List<MoveRequestDto> moves, final ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String end = resultSet.getString("end_position");
            String start = resultSet.getString("start_position");
            moves.add(new MoveRequestDto(start, end));
        }
    }

    public void reset(final Game game) throws SQLException {
        String query = "DELETE FROM move WHERE game = ?";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
        ) {
            statement.setString(1, game.getId());
            statement.executeUpdate();
        }
    }
}
