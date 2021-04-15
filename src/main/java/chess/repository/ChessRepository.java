package chess.repository;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.PieceMapper;
import chess.dto.ChessBoardDTO;
import chess.dto.RoomIdDTO;
import chess.dto.TurnDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChessRepository {

    private JdbcTemplate jdbcTemplate;

    public ChessRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public RoomIdDTO addGame(ChessGame chessGame) {
        String addQuery = "INSERT INTO chess_game (turn, finished, board) VALUES (?, ?, ?)";
        jdbcTemplate.update(addQuery, chessGame.getTurn(), chessGame.isOver(), serialize(chessGame));
        String findQuery = "SELECT count(*) FROM chess_game";
        return new RoomIdDTO(jdbcTemplate.queryForObject(findQuery, String.class));
    }

    public ChessBoardDTO loadGameAsDTO(String gameId) {
        String query = "SELECT board FROM chess_game WHERE id= ?";
        ChessBoardDTO chessBoardDTO = deserializeAsDTO(jdbcTemplate.queryForObject(query, String.class, gameId));
        return chessBoardDTO;
    }

    public ChessGame loadGame(String gameId) {
        String gameFindQuery = "SELECT board, turn FROM chess_game WHERE id= ?";
        return jdbcTemplate.queryForObject(gameFindQuery, (resultSet, rowNum) -> {
            ChessGame chessGame = new ChessGame(
                    deserialize(resultSet.getString("board")),
                    Color.of(resultSet.getString("turn")));
            return chessGame;
        }, gameId);
    }

    public TurnDTO turn(String gameId) {
        String query = "SELECT turn FROM chess_game WHERE id = ?";
        TurnDTO turnDTO = new TurnDTO(jdbcTemplate.queryForObject(query, String.class, gameId));
        return turnDTO;
    }

    public void saveGame(String gameId, ChessGame chessGame){
        String saveQuery = "UPDATE chess_game SET turn = ?, finished = ?, board = ? WHERE id = ?";
        jdbcTemplate.update(saveQuery, chessGame.getTurn(), chessGame.isOver(), serialize(chessGame), gameId);
    }


    //질문 여기가 맞는지?

    public String serialize(ChessGame chessGame) {
        return chessGame.getChessBoardMap()
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().getName() + entry.getKey().getStringPosition())
                .collect(Collectors.joining());
    }

    public ChessBoard deserialize(String response) {
        Map<Position, Piece> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += 3) {
            char name = response.charAt(i);
            String position = response.substring(i + 1, i + 3);
            Position piecePosition = Position.of(position);
            Piece piece = PieceMapper.of(name);
            chessBoard.put(piecePosition, piece);
        }
        return new ChessBoard(chessBoard);
    }

    public ChessBoardDTO deserializeAsDTO(String response) {
        Map<String, String> chessBoard = new LinkedHashMap<>();
        for (int i = 0; i < response.length(); i += 3) {
            String name = response.substring(i, i + 1);
            String position = response.substring(i + 1, i + 3);
            chessBoard.put(position, name);
        }
        return new ChessBoardDTO(chessBoard);
    }
}
