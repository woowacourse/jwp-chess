package wooteco.chess.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("turn")
public class TurnEntity {
    @Id
    private Long id;

    @Column("teamName")
    private String teamName;
}
