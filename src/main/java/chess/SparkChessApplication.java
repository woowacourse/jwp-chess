package chess;

import chess.web.WebRouter;

public class SparkChessApplication {

    private static final WebRouter router = new WebRouter();

    public static void main(String[] args) {
        router.init();
    }
}
