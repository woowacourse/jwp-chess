package wooteco.chess.domain.board;

import org.springframework.data.relational.core.mapping.Table;

@Table("board")
public class BoardEntity {
    private Long roomId;
    private String position;
    private String piece;
}
