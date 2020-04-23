package wooteco.chess;

import wooteco.chess.dao.ChessDao;
import wooteco.chess.dao.InMemoryChessDao;
import wooteco.chess.dao.MySqlChessDao;
import wooteco.chess.database.MySqlConnector;

import static spark.Spark.staticFiles;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFiles.location("/public");

        ChessDao chessDao = new InMemoryChessDao();
        if (MySqlConnector.getConnection() != null) {
            chessDao = new MySqlChessDao();
        }
//        SparkController controller = new SparkController(new ChessService());
//        controller.play();
    }
}
