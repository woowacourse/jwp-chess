//package wooteco.chess;
//
//import spark.ModelAndView;
//import spark.template.handlebars.HandlebarsTemplateEngine;
//import wooteco.chess.spark.webutil.WebRequest;
//
//import java.util.Map;
//
//import static spark.Spark.*;
//
//public class SparkChessApplication {
//
//    public static void main(final String[] args) {
//        staticFiles.location("/public");
//
//        get("/", (req, res) -> {
//            WebRequest webRequest = WebRequest.BLANK_BOARD;
//            return render(webRequest.generateModel(req), "index.hbs");
//        });
//
//        post("/", (req, res) -> {
//            WebRequest webRequest = WebRequest.of(req.queryParams("command"));
//            return render(webRequest.generateModel(req), "index.hbs");
//        });
//    }
//
//    private static String render(final Map<String, Object> model, final String templatePath) {
//        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
//    }
//}
