package chess.dto;

import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ChessGameDto {

    private final Map<String, Piece> boardForHtml;
    private final String name;
    private final String turn;
    private final Result result;

    public ChessGameDto(final String name, final ChessGame chessGame) {
        this.boardForHtml = convertBoardForHtml(chessGame.getCurrentBoard());
        this.name = name;
        this.turn = chessGame.getTurn().getNow().getValue();
        this.result = chessGame.generateResult();
    }

    private Map<String, Piece> convertBoardForHtml(final Map<Position, Piece> board) {
        return board.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> String.valueOf(entry.getKey().getColumn().getValue()) +
                                entry.getKey().getRow().getValue(),
                        Entry::getValue
                ));
    }

    public Map<String, Piece> getBoardForHtml() {
        return boardForHtml;
    }

    public String getName() {
        return name;
    }

    public String getTurn() {
        return turn;
    }

    public Result getResult() {
        return result;
    }
}
