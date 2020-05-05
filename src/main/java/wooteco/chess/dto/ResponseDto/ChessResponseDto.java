package wooteco.chess.dto.ResponseDto;

import wooteco.chess.consolView.PieceRender;
import wooteco.chess.domain.Chess;
import wooteco.chess.domain.board.Tile;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.Map;

public class ChessResponseDto {
    private Map<String, String> board;
    private double blackScore;
    private double whiteScore;
    private Team team;

    public static ChessResponseDto of(Chess chess) {
        ChessResponseDto chessResponseDto = new ChessResponseDto();
        chessResponseDto.setBlackScore(chess.calculateBlackScore());
        chessResponseDto.setWhiteScore(chess.calculateWhiteScore());
        chessResponseDto.setBoard(makeBoardToString(chess));
        chessResponseDto.setTeam(chess.getCurrentTeam());
        return chessResponseDto;
    }

    private static Map<String, String> makeBoardToString(Chess chess) {
        Map<String, String> boardInfo = new HashMap<>();
        for (Map.Entry<Coordinate, Tile> entry : chess.getChessBoard().entrySet()) {
            boardInfo.put(entry.getKey().toString(), PieceRender.findTokenByPiece(entry.getValue().getPiece()));
        }
        return boardInfo;
    }

    public Map<String, String> getBoard() {
        return board;
    }

    public void setBoard(Map<String, String> board) {
        this.board = board;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public void setBlackScore(double blackScore) {
        this.blackScore = blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public void setWhiteScore(double whiteScore) {
        this.whiteScore = whiteScore;
    }
}
