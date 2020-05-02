package spark;

import spark.controller.ChessWebController;
import spark.service.ChessService;

public class SparkUIChessApplication {
    public static void main(String[] args) {
        ChessService chessService = new ChessService();
        ChessWebController chessWebController = new ChessWebController(chessService);
    }
}
