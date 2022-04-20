package springchess.dao;

import springchess.model.piece.Piece;
import springchess.model.piece.Team;
import springchess.model.square.File;

import java.util.List;

public interface PieceDao<T> {

    Piece save(Piece piece, int squareId);

    T findBySquareId(int squareId);

    int updatePieceSquareId(int originSquareId, int newSquareId);

    int deletePieceBySquareId(int squareId);

    List<T> getAllPiecesByBoardId(int boardId);

    int countPawnsOnSameColumn(int roomId, File column, Team team);
}
