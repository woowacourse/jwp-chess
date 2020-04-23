package wooteco.chess;

import com.mysql.cj.jdbc.MysqlDataSource;
import wooteco.chess.controller.ChessWebController;
import wooteco.chess.dao.ChessDAO;
import wooteco.chess.dao.JDBCTemplate;
import wooteco.chess.service.ChessService;

import javax.sql.DataSource;

import static spark.Spark.staticFileLocation;

public class SparkChessApplication {
    public static void main(String[] args) {
        staticFileLocation("templates");
        DataSource mySqlDataSource = new MysqlDataSource();
        JDBCTemplate jdbcTemplate = new JDBCTemplate(mySqlDataSource);
        ChessDAO chessDAO = new ChessDAO(jdbcTemplate);
        ChessService chessService = new ChessService(chessDAO);
        ChessWebController chessWebController = new ChessWebController(chessService);
        chessWebController.run();
    }
}
