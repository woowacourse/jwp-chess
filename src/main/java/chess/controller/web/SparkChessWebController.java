package chess.controller.web;

import static spark.Spark.get;
import static spark.Spark.post;

import chess.dao.DbUserDao;
import chess.dao.UserDao;
import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.domain.board.position.Position;
import chess.domain.piece.Piece;
import chess.domain.user.User;
import chess.dto.response.web.BoardResponse;
import chess.service.UserService;
import chess.turndecider.AlternatingGameFlow;
import chess.turndecider.GameFlow;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkChessWebController {
    private static final Gson gson = new Gson();

    private static ChessBoard chessBoard;
    private static UserService userService;

    static {
        UserDao userDao = new DbUserDao();
        userService = new UserService(userDao);
    }

    private static ChessBoard generateChessBoard() {
        final BoardFactory boardFactory = RegularBoardFactory.getInstance();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory.create(), gameFlow);
    }

    public static void index() {
        get("/", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();
            return render(model, "index.html");
        });
    }

    public static void newGame() {
        get("/new-game", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();
            return render(model, "new_game.html");
        });
    }

    public static void openGame() {
        get("/open-game", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();
            return render(model, "open_game.html");
        });
    }

    public static void move() {
        post("/move", (request, response) -> {
            Position from = Position.of(request.queryParams("from"));
            Position to = Position.of(request.queryParams("to"));
            chessBoard.movePiece(from, to);

            Map<Position, Piece> movedBoard = chessBoard.getBoard();
            return BoardResponse.from(movedBoard);

        }, gson::toJson);
    }

    public static void currentTeam() {
        post("/current-team", (request, response) -> chessBoard.currentState().getName(), gson::toJson);
    }

    public static void users(Map<String, User> inGameUser) {
        get("/users", (request, response) -> inGameUser, gson::toJson);
    }

    public static void board() {
        final Map<Position, Piece> initBoard = RegularBoardFactory.getInstance().create();
        final BoardResponse initBoardResponse = BoardResponse.from(initBoard);
        get("/board", "application/json", (request, response) -> initBoardResponse, gson::toJson);
    }

    public static void playGame(Map<String, User> inGameUser) {
        post("/play-game", (request, response) -> {
            final Map<String, Object> model = new HashMap<>();

            User whitePlayer = new User(request.queryParams("white-player-name"));
            User blackPlayer = new User(request.queryParams("black-player-name"));

            userService.save(whitePlayer);
            userService.save(blackPlayer);

            inGameUser.clear();
            inGameUser.put("whiteUser", whitePlayer);
            inGameUser.put("blackUser", blackPlayer);

            chessBoard = generateChessBoard();

            return render(model, "game.html");
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
