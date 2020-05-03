package wooteco.chess.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("game_status")
public class GameStatus {

    @Id
    Long id;
    @Column
    String currentTurn;

    public Long getId() {
        return id;
    }

    public GameStatus(String currentTurn) {
        this.currentTurn = currentTurn;
    }

    public String getCurrentTurn() {
        return currentTurn;
    }
}
