package chess.spring.domain;

import static chess.web.domain.piece.type.PieceType.KING;
import static chess.web.domain.player.type.TeamColor.BLACK;
import static chess.web.domain.player.type.TeamColor.WHITE;

import chess.web.domain.board.Cell;
import chess.web.domain.board.move.MoveChecker;
import chess.web.domain.board.move.MoveRequest;
import chess.web.domain.piece.Piece;
import chess.web.domain.player.score.Scores;
import chess.web.domain.player.type.TeamColor;
import chess.web.domain.position.Position;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardNew {
    private static final int NUMBER_OF_ALL_KINGS = 2;
    private static final String EMPTY_CELL_STATUS = ".";

    private final Map<Position, Cell> cells = new HashMap<>();
    private final MoveChecker moveChecker;
    private final ScoreCalculatorNew scoreCalculator;

    public BoardNew(String boardStatus) {
        setCells(boardStatus);
        this.moveChecker = new MoveChecker();
        this.scoreCalculator = new ScoreCalculatorNew();
    }

    private void setCells(String boardStatus) {
        List<Position> positions = Position.getAllPositionsInOrder();
        for (int i = 0; i < positions.size(); i++) {
            setCell(positions.get(i), String.valueOf(boardStatus.charAt(i)));
        }
    }

    private void setCell(Position position, String cellStatus) {
        Piece piece = null;
        if (!cellStatus.equals(EMPTY_CELL_STATUS)) {
            piece = Piece.of(cellStatus);
        }
        cells.put(position, new Cell(piece));
    }

    public void movePiece(MoveRequest moveRequest) {
        validate(moveRequest);
        movePieceFromStartPositionCellToDestinationCell(moveRequest);
    }

    public void validate(MoveRequest moveRequest) {
        Cell startPositionCell = cells.get(moveRequest.getStartPosition());
        validateOwnPiece(startPositionCell, moveRequest.getCurrentTurnTeamColor());
        validateMoveRoute(moveRequest);
    }

    private void validateOwnPiece(Cell startPositionCell, TeamColor teamColor) {
        if (startPositionCell.isEmpty()) {
            throw new IllegalArgumentException("출발 위치에 기물이 존재하지 않습니다.");
        }
        if (startPositionCell.getTeamColor() != teamColor) {
            throw new IllegalArgumentException("자신의 기물이 아닙니다.");
        }
    }

    private void validateMoveRoute(MoveRequest moveRequest) {
        moveChecker.validateMove(moveRequest, cells);
    }

    private void movePieceFromStartPositionCellToDestinationCell(MoveRequest moveRequest) {
        Cell startPositionCell = cells.get(moveRequest.getStartPosition());
        cells.put(moveRequest.getDestination(), startPositionCell);
        cells.put(moveRequest.getStartPosition(), new Cell());
    }

    public Scores getScores() {
        return scoreCalculator.getCalculatedScore(cells);
    }

    public String getBoardStatus() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Position> positions = Position.getAllPositionsInOrder();
        for (Position position : positions) {
            Cell cell = cells.get(position);
            stringBuilder.append(cell.getStatus());
        }
        return stringBuilder.toString();
    }

    public boolean isKingDead() {
        List<String> cellsStatus = Arrays.stream(getBoardStatus().split(""))
            .collect(Collectors.toList());
        return cellsStatus.stream()
            .filter(cellStatus ->
                cellStatus.equals(KING.getName(WHITE))
                    || cellStatus.equals(KING.getName(BLACK)))
            .count() < NUMBER_OF_ALL_KINGS;
    }
}
