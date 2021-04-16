package chess.web.domain.piece.cache;

import static chess.web.domain.piece.type.PieceType.BISHOP;
import static chess.web.domain.piece.type.PieceType.KING;
import static chess.web.domain.piece.type.PieceType.KNIGHT;
import static chess.web.domain.piece.type.PieceType.PAWN;
import static chess.web.domain.piece.type.PieceType.QUEEN;
import static chess.web.domain.piece.type.PieceType.ROOK;

import chess.web.dao.piece.PieceRepository;
import chess.web.domain.piece.Piece;
import chess.web.domain.piece.type.PieceType;
import chess.web.domain.player.type.TeamColor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class PiecesCache {
    private static final List<Piece> PIECES = new ArrayList<>();
    private static final String PIECE_NOT_FOUND_ERROR_MESSAGE = "존재하지 않는 기물입니다.";

    private final PieceRepository pieceRepository;

    private PiecesCache(PieceRepository pieceRepository) {
        this.pieceRepository = pieceRepository;
    }

    @PostConstruct
    private void init() {
        try {
            cachePieces();
        } catch (SQLException e) {
            System.out.println("DB로부터 색깔별 기물 캐싱에 실패했습니다.");
            e.printStackTrace();
        }
    }

    private void cachePieces() throws SQLException {
        for (TeamColor teamColor : TeamColor.values()) {
            cachePiecesWithColor(teamColor);
        }
    }

    private void cachePiecesWithColor(TeamColor teamColor) throws SQLException {
        PIECES.addAll(getPiecesWithColorFromDB(teamColor));
    }

    private List<Piece> getPiecesWithColorFromDB(TeamColor teamColor) throws SQLException {
        return Arrays.asList(
            pieceRepository.findByPieceTypeAndTeamColor(PAWN, teamColor),
            pieceRepository.findByPieceTypeAndTeamColor(ROOK, teamColor),
            pieceRepository.findByPieceTypeAndTeamColor(KNIGHT, teamColor),
            pieceRepository.findByPieceTypeAndTeamColor(BISHOP, teamColor),
            pieceRepository.findByPieceTypeAndTeamColor(QUEEN, teamColor),
            pieceRepository.findByPieceTypeAndTeamColor(KING, teamColor));
    }


    public static Piece find(PieceType pieceType, TeamColor teamColor) {
        return PIECES.stream()
            .filter(piece ->
                piece.getPieceType() == pieceType
                    && piece.getTeamColor() == teamColor)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(PIECE_NOT_FOUND_ERROR_MESSAGE));
    }

    public static Piece findById(Long pieceId) {
        return PIECES.stream()
            .filter(piece -> piece.getId().equals(pieceId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(PIECE_NOT_FOUND_ERROR_MESSAGE));
    }
}
