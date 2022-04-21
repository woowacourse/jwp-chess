package chess.web;

//public class WebApplication {
//
//    public static void main(String[] args) {
//        port(8082);
//        staticFileLocation("/static");
//
//        ChessBoardDao chessBoardDao = new ChessBoardDao();
//        PlayerDao playerDao = new PlayerDao();
//        ChessGameService service = new ChessGameService(chessBoardDao, playerDao);
//        ChessGameController chessController = new ChessGameController(chessBoardDao, playerDao, service);
//
////        get("/", (req, res) -> render(chessController.root(req, res)));
//        get("/start", (req, res) -> render(chessController.start(req, res)));
//        get("/play", (req, res) -> render(chessController.play(req, res)));
//        get("/end", (req, res) -> render(chessController.end(req, res)));
//
//        post("/move", (req, res) -> render(chessController.move(req, res)));
//    }
//
//    private static String render(ModelAndView modelAndView) {
//        return new HandlebarsTemplateEngine().render(modelAndView);
//    }
//}
