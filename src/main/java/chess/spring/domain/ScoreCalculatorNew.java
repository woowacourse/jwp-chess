package chess.spring.domain;

import static chess.web.domain.piece.type.PieceType.PAWN;

import chess.web.domain.board.Cell;
import chess.web.domain.piece.Pawn;
import chess.web.domain.piece.Piece;
import chess.web.domain.player.score.Scores;
import chess.web.domain.player.type.TeamColor;
import chess.web.domain.position.Position;
import chess.web.domain.position.type.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ScoreCalculatorNew {

    private static final int MIN_COUNT_FOR_PAWN_HALF_SCORE = 2;

    public Scores getCalculatedScore(Map<Position, Cell> cells) {
        return new Scores(
            getPlayerScoreTeamColorOf(cells, TeamColor.WHITE),
            getPlayerScoreTeamColorOf(cells, TeamColor.BLACK));
    }

    private double getPlayerScoreTeamColorOf(Map<Position, Cell> cells, TeamColor teamColor) {
        Map<Position, Cell> cellsWithPieceOfExactColor = getCellsWithPieceOfExactColor(cells, teamColor);
        double totalScore = 0;
        for (File file : File.values()) {
            totalScore += getScoreOfFile(cellsWithPieceOfExactColor, file);
        }
        return totalScore;
    }

    private double getScoreOfFile(Map<Position, Cell> cells, File file) {
        List<Piece> allPiecesInFile = getAllPiecesInFile(cells, file);
        List<Piece> pawnsInFile = getAllPawnsInFile(allPiecesInFile);
        List<Piece> piecesInFileExceptPawn = getPiecesInFileExceptPawn(allPiecesInFile, pawnsInFile);
        double scoreOfPawnsInFile = getScoreOfPawnsInFile(pawnsInFile);
        double scoreOfPiecesExceptPawnInFile = getScoreOfPiecesExceptPawnInFile(piecesInFileExceptPawn);
        return scoreOfPawnsInFile + scoreOfPiecesExceptPawnInFile;
    }

    private List<Piece> getAllPiecesInFile(Map<Position, Cell> cells, File file) {
        List<Piece> pieces = new ArrayList<>();
        for (Entry<Position, Cell> positionCellEntry : cells.entrySet()) {
            addPiecesInFile(pieces, positionCellEntry, file);
        }
        return pieces;
    }

    private void addPiecesInFile(List<Piece> pieces, Entry<Position, Cell> positionCellEntry, File file) {
        Position position = positionCellEntry.getKey();
        Cell cell = positionCellEntry.getValue();
        if (position.getFile() == file) {
            pieces.add(cell.getPiece());
        }
    }

    private List<Piece> getAllPawnsInFile(List<Piece> allPiecesInFile) {
        return allPiecesInFile.stream()
            .filter(pieceInFile -> pieceInFile.getPieceType() == PAWN)
            .collect(Collectors.toList());
    }

    private List<Piece> getPiecesInFileExceptPawn(List<Piece> allPiecesInFile, List<Piece> pawnsInFile) {
        List<Piece> allPiecesInFileExceptPawn = new ArrayList<>(allPiecesInFile);
        allPiecesInFileExceptPawn.removeAll(pawnsInFile);
        return allPiecesInFileExceptPawn;
    }

    private double getScoreOfPawnsInFile(List<Piece> pawnsInFile) {
        int pawnsCountInFile = pawnsInFile.size();
        if (pawnsCountInFile >= MIN_COUNT_FOR_PAWN_HALF_SCORE) {
            return Pawn.halfScore() * (double) pawnsCountInFile;
        }
        return Pawn.defaultScore() * (double) pawnsCountInFile;
    }

    private double getScoreOfPiecesExceptPawnInFile(List<Piece> piecesInFileExceptPawn) {
        double scoreSum = 0;
        for (Piece piece : piecesInFileExceptPawn) {
            scoreSum += piece.getScore();
        }
        return scoreSum;
    }

    private Map<Position, Cell> getCellsWithPieceOfExactColor(Map<Position, Cell> cells, TeamColor teamColor) {
        Map<Position, Cell> cellsWithPieceOfExactColor = new HashMap<>();
        for (Entry<Position, Cell> positionCellEntry : cells.entrySet()) {
            addCellWithPieceOfExactColor(cellsWithPieceOfExactColor, positionCellEntry, teamColor);
        }
        return cellsWithPieceOfExactColor;
    }

    private void addCellWithPieceOfExactColor(Map<Position, Cell> cellsWithPieceOfExactColor, Entry<Position, Cell> positionCellEntry, TeamColor teamColor) {
        Position position = positionCellEntry.getKey();
        Cell cell = positionCellEntry.getValue();
        if (cell.containsPieceColorOf(teamColor)) {
            cellsWithPieceOfExactColor.put(position, cell);
        }
    }
}
