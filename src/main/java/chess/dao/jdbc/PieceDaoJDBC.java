package chess.dao.jdbc;

import chess.dao.PieceDao;
import chess.dao.dto.piece.PieceDto;
import chess.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PieceDaoJDBC implements PieceDao {

    @Override
    public Long save(PieceDto pieceDto) {
        final String query = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, pieceDto.getGameId().intValue());
            pstmt.setString(2, pieceDto.getPosition());
            pstmt.setString(3, pieceDto.getSymbol());
            pstmt.addBatch();
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("체스말을 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public long[] savePieces(final Long gameId, final List<PieceDto> pieceDtos) {
        final String query = "INSERT INTO piece(game_id, position, symbol) VALUES (?, ?, ?)";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (PieceDto pieceDto : pieceDtos) {
                pstmt.setInt(1, gameId.intValue());
                pstmt.setString(2, pieceDto.getPosition());
                pstmt.setString(3, pieceDto.getSymbol());
                pstmt.addBatch();
            }
            return pstmt.executeLargeBatch();
        } catch (SQLException e) {
            throw new DataAccessException("체스말을 저장하는데 실패했습니다.", e);
        }
    }

    @Override
    public Long updateByGameIdAndPosition(PieceDto pieceDto) {
        final String query = "UPDATE piece SET symbol = ? WHERE game_id=? && position=?";

        try (final Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, pieceDto.getSymbol());
            pstmt.setInt(2, pieceDto.getGameId().intValue());
            pstmt.setString(3, pieceDto.getPosition());
            return pstmt.executeLargeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("선택한 위치의 체스말을 수정하는데 실패했습니다.", e);
        }
    }

    @Override
    public List<PieceDto> findPiecesByGameId(final Long gameId) {
        final String query = "SELECT * from piece where game_id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, gameId.intValue());
            List<PieceDto> pieceResponseDtos = new ArrayList<>();

            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    pieceResponseDtos.add(new PieceDto(
                            resultSet.getLong("id"),
                            resultSet.getLong("game_id"),
                            resultSet.getString("symbol"),
                            resultSet.getString("position")));
                }
                return pieceResponseDtos;
            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID에 해당하는 체스말들을 검색하는데 실패했습니다.", e);
        }
    }

    @Override
    public PieceDto findById(Long id) {
        final String query = "SELECT * from piece where id = ?";

        try (Connection connection = ConnectionProvider.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);) {
            pstmt.setInt(1, id.intValue());

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                return new PieceDto(
                        resultSet.getLong("id"),
                        resultSet.getLong("game_id"),
                        resultSet.getString("symbol"),
                        resultSet.getString("position"));

            }
        } catch (SQLException e) {
            throw new DataAccessException("해당 GameID에 해당하는 체스말들을 검색하는데 실패했습니다.", e);
        }
    }
}
