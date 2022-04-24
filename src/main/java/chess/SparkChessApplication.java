package chess;

import static spark.Spark.staticFiles;

import chess.controller.web.SparkChessWebController;
import chess.domain.board.ChessBoard;
import chess.domain.user.User;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;

public class SparkChessApplication {

    private static final Gson gson = new Gson();
    private static ChessBoard chessBoard;

    public static void main(String[] args) {
        staticFiles.location("/static");

        SparkChessWebController.index();
        SparkChessWebController.newGame();
        SparkChessWebController.openGame();
        SparkChessWebController.move();
        SparkChessWebController.currentTeam();
        final Map<String, User> inGameUser = new HashMap<>();
        SparkChessWebController.playGame(inGameUser);
        SparkChessWebController.users(inGameUser);
        SparkChessWebController.board();
    }
}
