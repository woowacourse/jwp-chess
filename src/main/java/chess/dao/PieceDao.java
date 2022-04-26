package chess.dao;

import java.util.List;

public interface PieceDao {
    void initBoard(int gameId);

    List<PieceEntity> getBoardByGameId(int id);

    void remove(int id);

    void update(PieceEntity piece, int gameId);
}
