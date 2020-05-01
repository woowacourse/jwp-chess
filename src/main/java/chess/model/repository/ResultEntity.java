package chess.model.repository;

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

    public void addWin(Integer winCount) {
        this.win += winCount;
    }


    public void addDraw(Integer drawCount) {
        this.draw += drawCount;
    }

    public void addLose(Integer loseCount) {
        this.lose += loseCount;
    }
}