package chess.repository;

import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.File;

import java.util.List;

public interface PieceRepository<T> {

    Piece save(Piece piece, int squareId);

    T findBySquareId(int squareId);

    int updatePieceSquareId(int originSquareId, int newSquareId);

    int deletePieceBySquareId(int squareId);

    List<T> getAllPiecesByBoardId(int boardId);

    int countPawnsOnSameColumn(int roomId, File column, Team team);
}
