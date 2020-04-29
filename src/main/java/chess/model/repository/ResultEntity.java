package chess.model.repository;

import chess.model.domain.board.TeamScore;
import chess.model.domain.piece.Team;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("RESULT_TB")
public class ResultEntity {

    @Id
    private Integer id;
    @Column("USER_NM")
    private String userName;
    private Integer win;
    private Integer draw;
    private Integer lose;

    public ResultEntity() {
    }

    public ResultEntity(String userName, Integer win, Integer draw, Integer lose) {
        this.userName = userName;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
    }

    public ResultEntity(String userName) {
        this(userName, 0, 0, 0);
    }

    public String getUserName() {
        return userName;
    }

    public Integer getWin() {
        return win;
    }

    public Integer getDraw() {
        return draw;
    }

    public Integer getLose() {
        return lose;
    }

    public void updateWinOrLose(TeamScore teamScore, Team team) {
        this.win += convertScore(teamScore.isWin(team));
        this.draw += convertScore(teamScore.isDraw());
        this.lose += convertScore(teamScore.isLose(team));
    }

    private int convertScore(boolean condition) {
        if (condition) {
            return 1;
        }
        return 0;
    }
}