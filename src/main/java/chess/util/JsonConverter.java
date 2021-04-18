package chess.util;

import chess.domain.board.Position;
import chess.domain.piece.Piece;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public class JsonConverter {
    private static final com.google.gson.Gson gson = new com.google.gson.Gson();

    public static JsonObject fromJson(String jsonString) {
        return gson.fromJson(jsonString, JsonObject.class);
    }

    public static JsonObject fromJson(JsonElement jsonElement) {
        return gson.fromJson(jsonElement, JsonObject.class);
    }

    public static JsonObject toJsonObject(Map<Position, Piece> chessBoard) {
        JsonObject chessBoardJson = new JsonObject();
        for (Map.Entry<Position, Piece> entry : chessBoard.entrySet()) {
            String position = entry.getKey().getPosition();

            Piece piece = entry.getValue();
            JsonObject pieceJson = new JsonObject();
            pieceJson.addProperty("type", piece.getType());
            pieceJson.addProperty("color", piece.getColor());

            chessBoardJson.add(position, pieceJson);
        }
        return chessBoardJson;
    }
}
