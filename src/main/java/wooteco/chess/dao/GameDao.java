package wooteco.chess.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import wooteco.chess.domain.GameManager;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Color;
import wooteco.chess.dto.GameManagerDto;

public class GameDao {
	public int addGame(GameManagerDto gameManagerDto) {
		int roomNo = ThreadLocalRandom.current()
			.ints(100000, 999999)
			.findFirst()
			.orElse(0);

		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "INSERT INTO chessgame (board, turn,roomno) VALUES (?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, gameManagerDto.getBoard());
			statement.setString(2, gameManagerDto.getTurn());
			statement.setString(3, String.valueOf(roomNo));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}

		return roomNo;
	}

	public GameManager findGame(int roomNo) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "SELECT * FROM chessgame WHERE roomno = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, String.valueOf(roomNo));
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

	public void updateGame(GameManagerDto gameManagerDto, int roomNo) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "UPDATE chessgame SET board = ?, turn = ? WHERE roomno = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, gameManagerDto.getBoard());
			statement.setString(2, gameManagerDto.getTurn());
			statement.setString(3, String.valueOf(roomNo));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void deleteGame(int roomNo) {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "DELETE FROM chessgame WHERE roomno = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, String.valueOf(roomNo));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<String> findAllRoomNo() {
		try (Connection connection = new SQLConnector().getConnection()) {
			String query = "SELECT roomno FROM chessgame";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet result = statement.executeQuery();
			List<String> roomNumbers = new ArrayList<>();
			while (result.next()) {
				roomNumbers.add(result.getString("roomno"));
			}
			return roomNumbers;
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
