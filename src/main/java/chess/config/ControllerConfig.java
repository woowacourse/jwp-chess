package chess.config;

import chess.controller.SparkChessController;

public class ControllerConfig {

    public static SparkChessController getWebController() {
        return new SparkChessController(ServiceConfig.getGameService());
    }
}
