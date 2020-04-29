package wooteco.chess;

import static spark.Spark.*;

import java.sql.SQLException;

import wooteco.chess.controller.SparkChessController;
import wooteco.chess.dao.GameDao;
import wooteco.chess.dao.MoveDao;
import wooteco.chess.dao.PlayerDao;
import wooteco.chess.service.ChessService;
import wooteco.chess.service.SparkChessService;
import wooteco.chess.util.RoutesConfig;

public class SparkChessApplication {

    public static void main(String[] args) {
        RoutesConfig.configure();
        addTemporaryPlayers();
        before(RoutesConfig::setJsonContentType);
        ChessService service = new SparkChessService(new GameDao(), new MoveDao(), new PlayerDao());
        new SparkChessController(service);
    }

    private static void addTemporaryPlayers() {
        // 플레이어 회원가입 / 로그인 구현 이전 foreign key 오류를 내지 않기 위해 임시로 DB에 플레이어 추가
        try {
            new PlayerDao().addInitialPlayers();
        } catch (SQLException ignored) {
        }
    }
}
