package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.web.dao.piece.PieceRepository;
import chess.domain.piece.Piece;
import chess.domain.piece.type.PieceType;
import chess.domain.color.type.TeamColor;
import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PieceDAOTest {
    @Autowired
    private PieceRepository pieceRepository;

    @DisplayName("모든 색깔과 종류의 기물들이 DB에서 조회되는지 테스트")
    @Test
    void findById() throws SQLException {
        for (PieceType pieceType : PieceType.values()) {
            findByTeamColorAndPieceTypeOf(pieceType);
        }
    }

    private void findByTeamColorAndPieceTypeOf(PieceType pieceType) throws SQLException {
        for (TeamColor teamColor : TeamColor.values()) {
            Piece piece = pieceRepository.findByPieceTypeAndTeamColor(pieceType, teamColor);

            assertThat(piece.getPieceType()).isSameAs(pieceType);
            assertThat(piece.getTeamColor()).isSameAs(teamColor);
        }
    }
}