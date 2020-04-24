package wooteco.chess.spark.webutil;

import spark.Request;
import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.spark.sparkservice.ChessService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public enum WebRequest {

    BLANK_BOARD("빈 판", req -> {
        Map<String, Object> model = ModelParser.parseBlankBoard();
        model.putAll(ModelParser.parseMovablePlaces(new ArrayList<>()));
        return model;
    }),
    NEW_GAME("새 게임", req -> {
        ChessService chessService = ChessService.getInstance();

        return ModelParser.parseBoard(chessService.newGame());
    }),
    LOAD_GAME("불러오기", req -> {
        ChessService chessService = ChessService.getInstance();

        return ModelParser.parseBoard(chessService.readBoard());
    }),
    STATUS("현재점수", req -> {
        ChessService chessService = ChessService.getInstance();
        Board board = chessService.readBoard();

        Map<String, Object> model = ModelParser.parseBoard(board);
        model.put("player1_info", "WHITE: " + chessService.calculateScore(Team.WHITE));
        model.put("player2_info", "BLACK: " + chessService.calculateScore(Team.BLACK));
        return model;
    }),
    MOVE("이동", req -> {
        ChessService chessService = ChessService.getInstance();

        Position start = Position.of(req.queryParams("start"));
        Position end = Position.of(req.queryParams("end"));
        tryMove(start, end);

        return ModelParser.parseBoard(chessService.readBoard());
    }),
    SHOW_MOVABLE("갈 수 있는 곳 확인", req -> {
        ChessService chessService = ChessService.getInstance();

        Position start = Position.of(req.queryParams("start"));
        List<Position> movablePositions = tryFindMovablePlaces(start);

        Map<String, Object> model = ModelParser.parseBoard(chessService.readBoard(), movablePositions);
        if (movablePositions.size() != 0) {
            model.put("start", req.queryParams("start"));
        }
        return model;
    });

    private String name;
    private ThrowingFunction generateModel;

    WebRequest(final String name, final ThrowingFunction generateModel) {
        this.name = name;
        this.generateModel = generateModel;
    }

    public static WebRequest of(final String commandName) {
        return Stream.of(WebRequest.values())
                .filter(webRequest -> webRequest.name.equals(commandName))
                .findAny()
                .orElse(WebRequest.BLANK_BOARD);
    }

    private static void tryMove(final Position start, final Position end) throws SQLException {
        ChessService chessService = ChessService.getInstance();

        try {
            chessService.move(start, end);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static List<Position> tryFindMovablePlaces(final Position start) throws SQLException {
        ChessService chessService = ChessService.getInstance();

        try {
            return chessService.findMovablePlaces(start);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    public Map<String, Object> generateModel(final Request request) throws SQLException {
        return this.generateModel.action(request);
    }

    private interface ThrowingFunction {
        Map<String, Object> action(Request request) throws SQLException;
    }
}
