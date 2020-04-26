package wooteco.chess.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("turn")
public class Turn {
    @Column("teamName")
    private String teamName;
}
