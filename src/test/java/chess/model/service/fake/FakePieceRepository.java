package chess.model.service.fake;

import chess.model.piece.Pawn;
import chess.model.piece.Piece;
import chess.model.piece.Team;
import chess.model.square.File;
import chess.repository.PieceRepository;

import java.util.List;

public class FakePieceRepository implements PieceRepository<Piece> {

    @Override
    public Piece save(Piece piece, int squareId) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public Piece findBySquareId(int squareId) {
        return new Pawn(Team.WHITE);
    }

    @Override
    public int updatePieceSquareId(int originSquareId, int newSquareId) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public int deletePieceBySquareId(int squareId) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public List<Piece> getAllPiecesByBoardId(int boardId) {
        return List.of(new Pawn(Team.WHITE));
    }

    @Override
    public int countPawnsOnSameFile(int roomId, File column, Team team) {
        throw new UnsupportedOperationException("테스트에서 사용하지 않는 메서드입니다.");
    }

    @Override
    public int saveAllPieces(List<Piece> pieces) {
        return 64;
    }
}
