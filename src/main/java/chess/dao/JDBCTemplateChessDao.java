package chess.dao;

import chess.dto.GameRoomDto;
import chess.dto.MoveRequestDto;
import chess.dto.NewGameRequest;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCTemplateChessDao implements ChessDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JDBCTemplateChessDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Map<String, String> getBoardByGameId(String gameId) {
        if (exists(gameId)) {
            return findBoardByGameId(gameId);
        }
        return createNewBoard(gameId);
    }

    private boolean exists(String gameId) {
        final String sql = "select count(1) as isExists from game where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, gameId) == 1;
    }

    private Map<String, String> findBoardByGameId(String gameId) {
        final String sql = "select position, piece from board where game_id = ?";
        return jdbcTemplate.query(sql, positionsToBoard(), gameId);
    }

    private ResultSetExtractor<Map<String, String>> positionsToBoard() {
        return (ResultSet resultSet) -> {
            Map<String, String> board = new HashMap<>();
            while (resultSet.next()) {
                board.put(resultSet.getString("position"), resultSet.getString("piece"));
            }
            return board;
        };
    }

    private Map<String, String> createNewBoard(String gameId) {
        createGameNumber(gameId);
        insertPiecesOnPositions(gameId);
        return findBoardByGameId(gameId);
    }

    private void createGameNumber(String gameId) {
        final String sql = "insert into game(id) value (?)";
        jdbcTemplate.update(sql, gameId);
    }

    private void insertPiecesOnPositions(String gameId) {
        final String sql = "insert into board(game_id, position, piece) "
                + "select ?, initialPosition, initialPiece from initialBoard";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public void move(MoveRequestDto moveRequestDto) {
        deleteFromAndTo(moveRequestDto);
        insertTo(moveRequestDto);
        changeTurn(moveRequestDto.getGameId());
    }

    private void deleteFromAndTo(MoveRequestDto moveRequestDto) {
        final String sql = "delete from board where game_id = :gameId and position in (:from, :to)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequestDto));
    }

    private void insertTo(MoveRequestDto moveRequestDto) {
        final String sql = "insert into board values (:gameId, :to, :piece)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(moveRequestDto));
    }

    @Override
    public String getTurnByGameId(String gameId) {
        final String sql = "select turn from game where id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, gameId);
    }

    private void changeTurn(String gameId) {
        final String sql = "update game set turn = "
                + "case when turn = 'black' then 'white' when turn = 'white' then 'black' end where id = ?";
        jdbcTemplate.update(sql, gameId);
    }

    @Override
    public int createNewGame(NewGameRequest newGameRequest) {
        final String sql = "insert into game(room_name, room_password, white_name, black_name) values(:roomName, :roomPassword, :whiteName, :blackName)";
        namedParameterJdbcTemplate.update(sql, new BeanPropertySqlParameterSource(newGameRequest));
        final int newRoomId = findRoomIdByRoomName(newGameRequest.getRoomName());
        insertPiecesOnPositions(String.valueOf(newRoomId));
        return newRoomId;
    }

    private int findRoomIdByRoomName(String roomName) {
        final String sql = "select id from game where room_name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, roomName);
    }

    @Override
    public List<GameRoomDto> findGamesOnPlay() {
        final String sql = "select id, room_name, white_name, black_name from game where deleted = 0 and winner = ''";
        return jdbcTemplate.query(sql, gameRoomDtoMapper());
    }

    private ResultSetExtractor<List<GameRoomDto>> gameRoomDtoMapper() {
        return (ResultSet resultSet) -> {
            List<GameRoomDto> gameRoomDtos = new ArrayList<>();
            while (resultSet.next()) {
                gameRoomDtos.add(new GameRoomDto(resultSet.getInt("id"),
                        resultSet.getString("room_name"),
                        resultSet.getString("white_name"),
                        resultSet.getString("black_name")));
            }
            return gameRoomDtos;
        };
    }

    @Override
    public GameRoomDto findGameById(int id) {
        final String sql = "select id, room_name, white_name, black_name from game where id = ?";
        return jdbcTemplate.queryForObject(sql,
                (resultSet, rowNum) -> {
                    return new GameRoomDto(resultSet.getInt("id"),
                            resultSet.getString("room_name"),
                            resultSet.getString("white_name"),
                            resultSet.getString("black_name"));
                },
                id);
    }

    @Override
    public int deleteGameById(int id) {
        final String sql = "delete from game where id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }
}
