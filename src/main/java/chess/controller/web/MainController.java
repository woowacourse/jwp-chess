//package chess.controller.web;
//
//import chess.view.OutputView;
//import org.springframework.stereotype.Controller;
//
//import java.sql.SQLException;
//
//import static spark.Spark.exception;
//import static spark.Spark.get;
//
//@Controller
//public class MainController {
//
//    public MainController() {
//    }
//
//    public void mapping() {
//        handlingSQLException();
//        handlingIllegalArgumentException();
//        handlingPageNotFoundError();
//    }
//
//    private void handlingSQLException() {
//        exception(SQLException.class, (e, req, res) -> {
//            res.status(404);
//            res.body("SQL error : " + e.getMessage());
//        });
//    }
//
//    private void handlingIllegalArgumentException() {
//        exception(IllegalArgumentException.class, (e, req, res) -> {
//            res.status(404);
//            res.body("Unexpected error : " + e.getMessage());
//        });
//    }
//
//    private void handlingPageNotFoundError() {
//        get("*", (req, res) -> {
//            if (req.pathInfo().startsWith("/static")) {
//                return null;
//            }
//            res.status(404);
////            return OutputView.printErrorMessage("Page not found");
//        });
//    }
//}
