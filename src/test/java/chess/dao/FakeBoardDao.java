package chess.dao;

import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.dto.response.PieceResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FakeBoardDao implements BoardDao {

    private final Map<Long, FakePiece> boards = new HashMap<>();

    private long id = 0L;

    @Override
    public void savePieces(Map<Position, Piece> board, long roomId) {
        for (Position position : board.keySet()) {
            id++;
            boards.put(id, new FakePiece(position.getPositionToString(), board.get(position).getSymbol(), roomId));
        }
    }

    @Override
    public List<PieceResponse> findAllPiece(long roomId) {
        List<PieceResponse> pieceResponses = new ArrayList<>();
        List<FakePiece> fakePieces = boards.keySet()
                .stream()
                .filter(key -> boards.get(key).getRoomId() == roomId)
                .map(boards::get)
                .collect(Collectors.toList());

        for (FakePiece fakePiece : fakePieces) {
            pieceResponses.add(new PieceResponse(fakePiece.getPosition(), fakePiece.getSymbol()));
        }
        return pieceResponses;
    }

    @Override
    public void updatePosition(String symbol, String destination, long roomId) {
        for (Long idx : boards.keySet()) {
            if (boards.get(idx).getPosition().equals(destination) && boards.get(idx).getRoomId() == roomId) {
                boards.put(idx, new FakePiece(symbol, destination, roomId));
            }
        }
    }

    @Override
    public void deleteBoard() {
        boards.clear();
    }
}
