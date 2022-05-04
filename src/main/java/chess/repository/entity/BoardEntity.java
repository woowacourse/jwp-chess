package chess.repository.entity;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BoardEntity {

    private final String gameRoomId;
    private final String positionColumnValue;
    private final int positionRowValue;
    private final String pieceName;
    private final String pieceTeamValue;

    public BoardEntity(String gameRoomId,
                       String positionColumnValue,
                       int positionRowValue,
                       String pieceName,
                       String pieceTeamValue) {
        this.gameRoomId = gameRoomId;
        this.positionColumnValue = positionColumnValue;
        this.positionRowValue = positionRowValue;
        this.pieceName = pieceName;
        this.pieceTeamValue = pieceTeamValue;
    }

    public BoardEntity(String gameRoomId,
                       char columnValue,
                       int rowValue,
                       Map<Position, Piece> currentBoard) {
        this.gameRoomId = gameRoomId;
        positionColumnValue = String.valueOf(columnValue);
        positionRowValue = rowValue;
        Piece piece = currentBoard.get(Position.of(columnValue, positionRowValue));
        pieceName = piece.getName();
        pieceTeamValue = piece.getTeam().getValue();
    }

    public static List<BoardEntity> generateBoardEntities(String gameRoomId, Map<Position, Piece> board) {
        List<BoardEntity> boardEntities = new ArrayList<>();
        for (Position position : board.keySet()) {
            Piece piece = board.get(position);
            BoardEntity boardEntity = new BoardEntity(
                    gameRoomId,
                    String.valueOf(position.getColumn().getValue()),
                    position.getRow().getValue(),
                    piece.getName(),
                    piece.getTeam().getValue()
            );
            boardEntities.add(boardEntity);
        }
        return boardEntities;
    }

    public String getGameRoomId() {
        return gameRoomId;
    }

    public String getPositionColumnValue() {
        return positionColumnValue;
    }

    public int getPositionRowValue() {
        return positionRowValue;
    }

    public String getPieceName() {
        return pieceName;
    }

    public String getPieceTeamValue() {
        return pieceTeamValue;
    }
}
