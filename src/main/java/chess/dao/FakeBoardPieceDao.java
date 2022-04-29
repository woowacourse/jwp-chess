package chess.dao;

import static chess.util.RandomCreationUtils.createUuid;

import chess.domain.db.BoardPiece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FakeBoardPieceDao implements BoardPieceDao{

    private List<BoardPiece> boardPieces = new ArrayList<>();

    @Override
    public void save(String gameId, Map<String, String> positionToPiece) {
        Set<String> positions = positionToPiece.keySet();

        for (String position : positions) {
            BoardPiece boardPiece = new BoardPiece(createUuid(), gameId, position, positionToPiece.get(position));
            boardPieces.add(boardPiece);
        }
    }

    @Override
    public List<BoardPiece> findLastBoardPiece(String lastGameId) {
        return boardPieces.stream()
                .filter(boardPiece -> lastGameId.equals(boardPiece.getGameId()))
                .collect(Collectors.toUnmodifiableList());
    }
}
