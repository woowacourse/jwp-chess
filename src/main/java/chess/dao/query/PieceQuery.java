package chess.dao.query;

public enum PieceQuery {

    SELECT_ALL_PIECE("select * from piece where game_id = ?"),

    INSERT_PIECE("insert into piece (position, type, color, game_id) values (?, ?, ?, ?)"),

    UPDATE_PIECE_POSITION("update piece set position = ? where game_id = ? and position = ?"),

    DELETE_PIECE("delete from piece where game_id = ? and position = ?"),
    DELETE_ALL_PIECE("delete from piece where game_id = ?"),
    ;

    private final String value;

    PieceQuery(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
