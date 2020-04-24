package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;

public class GameDao {
	public void addGame(GameManagerDto gameManagerDto) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "INSERT INTO chessgame (board, turn) VALUES (?, ?) ON DUPLICATE KEY UPDATE board = ?,turn = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, gameManagerDto.getBoard());
			statement.setString(2, gameManagerDto.getTurn());
			statement.setString(3, gameManagerDto.getBoard());
			statement.setString(4, gameManagerDto.getTurn());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public GameManager findGame(long id) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "SELECT * FROM chessgame WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, String.valueOf(id));
			ResultSet result = statement.executeQuery();
			if (!result.next()) {
				return null;
			}
			String board = result.getString("board");
			String turn = result.getString("turn");
			return new GameManager(BoardFactory.of(board), Color.of(turn));
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void updateGame(GameManagerDto gameManagerDto) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "UPDATE chessgame SET board = ?, turn = ? WHERE id = 1";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, gameManagerDto.getBoard());
			statement.setString(2, gameManagerDto.getTurn());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
