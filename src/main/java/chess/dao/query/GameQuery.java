package chess.dao.query;

public enum GameQuery {

    SELECT_GAME("select * from game where game_id = ?"),
    SELECT_ALL_GAME("select * from game"),
    SELECT_GAME_PASSWORD("select password from game where game_id = ?"),
    SELECT_LAST_GAME_ID("select game_id from game order by game_id desc limit 1"),

    INSERT_GAME("insert into game (game_id, title, password, turn, status) values (?, ?, ?, ?, ?)"),

    UPDATE_GAME("update game set turn = ?, status = ? where game_id = ?"),
    UPDATE_GAME_STATUS("update game set status = ? where game_id = ?"),

    DELETE_GAME("delete from game where game_id = ?"),
    ;

    private final String value;

    GameQuery(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
