package chess.controller;

import chess.domain.board.Position;
import chess.domain.feature.Color;
import chess.domain.game.ChessGame;
import chess.domain.game.Result;
import chess.domain.piece.Piece;
import chess.dto.OutcomeResponseDto;
import chess.dto.PieceResponseDto;
import chess.dto.ScoreResponseDto;
import chess.dto.TurnResponseDto;

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
        model.put("turn", new TurnResponseDto(turn));

        Map<Position, Piece> chessBoard = chessGame.getChessBoardAsMap();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            model.put(entry.getKey().getPosition(), new PieceResponseDto(entry.getValue()));
        }

        Result result = chessGame.calculateResult();
        model.put("score", new ScoreResponseDto(result));
        if (!chessGame.isOngoing()) {
            model.put("outcome", new OutcomeResponseDto(result));
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
