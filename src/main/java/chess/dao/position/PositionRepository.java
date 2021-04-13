package chess.dao.position;

import chess.domain.position.Position;
import chess.domain.position.type.File;
import chess.domain.position.type.Rank;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository {
    Position findByFileAndRank(File file, Rank rank) throws SQLException;
}
