package chess.web.dao.position;

import chess.web.domain.position.Position;
import chess.web.domain.position.type.File;
import chess.web.domain.position.type.Rank;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository {
    Position findByFileAndRank(File file, Rank rank) throws SQLException;
}
