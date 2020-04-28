package wooteco.chess.controller;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import wooteco.chess.result.Result;
import wooteco.chess.service.ChessService;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.MoveDto;

public class ChessController {
    private ChessService chessService = new ChessService();

    public Object move(Request request, Response response) {
        int roomId = Integer.parseInt(request.queryParams("roomId"));
        Coordinate source = Coordinate.of(request.queryParams("source"));
        Coordinate target = Coordinate.of(request.queryParams("target"));
        Result result = chessService.move(new MoveDto(roomId, source, target));
        if (result.isSuccess()) {
            return result.getObject();
        }
        response.body(result.getObject().toString());
        response.status(409);
        return response;
    }

    public Object getMovableWay(Request request, Response response) {
        int roomId = Integer.parseInt(request.queryParams("roomId"));
        Team team = Team.valueOf(request.queryParams("team"));
        Coordinate coordinate = Coordinate.of(request.queryParams("coordinate"));
        Result result = chessService.getMovableWay(roomId, team, coordinate);
        if (result.isSuccess()) {
            return new Gson().toJson(result.getObject());
        }
        response.body(result.getObject().toString());
        response.status(409);
        return response;
    }

    public Object renew(Request request, Response response) {
        int roomId = Integer.parseInt(request.queryParams("roomId"));
        Result result = chessService.renew(roomId);
        if (result.isSuccess()) {
            return new Gson().toJson(result.getObject());
        }
        response.body(result.getObject().toString());
        response.status(409);
        return response;
    }
}
