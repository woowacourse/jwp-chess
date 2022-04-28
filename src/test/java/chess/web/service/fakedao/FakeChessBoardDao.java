package chess.web.service.fakedao;

import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.web.dao.ChessBoardDao;
import chess.web.dto.ChessCellDto;
import chess.web.dto.MoveDto;
import java.util.HashMap;
import java.util.Map;

public class FakeChessBoardDao implements ChessBoardDao {

    Map<Position, Piece> repository = new HashMap<>();

    @Override
    public void save(Position position, Piece piece, int roomId) {
        repository.put(position, piece);
    }

    @Override
    public Map<Position, Piece> findAllPieces(int roomId) {
        return repository;
    }

    @Override
    public void movePiece(MoveDto moveDto, int roomId) {

    }

    @Override
    public ChessCellDto findByPosition(int roomId, String position) {
        return null;
    }

    @Override
    public boolean boardExistInRoom(int roomId) {
        return true;
    }
}
