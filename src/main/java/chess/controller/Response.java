package chess.controller;

import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.OutcomeDTO;
import chess.dto.PieceDTO;
import chess.dto.ScoreDTO;
import chess.dto.TurnDTO;

import java.util.HashMap;
import java.util.Map;

public class Response {
    private final Map<String, Object> model;
    private final StatusCode httpStatus;

    public Response(StatusCode httpStatus) {
        this.model = new HashMap<>();
        this.httpStatus = httpStatus;
    }

    public Response(ChessGame chessGame, StatusCode httpStatus) {
        this.model = pieceMoveModelToRender(chessGame);
        this.httpStatus = httpStatus;
    }

    private Map<String, Object> pieceMoveModelToRender(ChessGame chessGame) {
        Map<String, Object> model = new HashMap<>();
        Color turn = chessGame.getTurn();
        model.put("turn", new TurnDTO(turn));

        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            model.put(entry.getKey().getPosition(), new PieceDTO(entry.getValue()));
        }

        Result result = chessGame.calculateResult();
        model.put("score", new ScoreDTO(result));
        if (!chessGame.isOngoing()) {
            model.put("outcome", new OutcomeDTO(result));
        }
        return model;
    }

    public boolean isNotSuccessful() {
        return !StatusCode.SUCCESSFUL.equals(httpStatus);
    }

    public void add(String key, Object value) {
        model.put(key, value);
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public int getHttpStatus() {
        return httpStatus.getCode();
    }
}
