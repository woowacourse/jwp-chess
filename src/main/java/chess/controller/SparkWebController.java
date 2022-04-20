package chess.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Result;
import chess.domain.piece.Piece;
import chess.domain.square.Rank;
import chess.dto.GameResultDto;
import chess.dto.RankDto;
import chess.dto.ResponseDto;
import chess.service.GameService;
import chess.service.MemberService;
import chess.util.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class SparkWebController {
    
    private final GameService gameService;
    private final MemberService memberService;

    public SparkWebController(final GameService gameService, final MemberService memberService) {
        this.gameService = gameService;
        this.memberService = memberService;
    }

    private static String render(final Map<String, Object> model, final String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }

    public void run() {
        staticFiles.location("/static");
        /** 웹 페이지 */
        get("/", this::renderHome);
        get("/play/:gameId", this::renderPlayGame);
        get("/result/:gameId", this::renderGameResult);
        get("/history/:memberId", this::renderMemberHistory);
        get("/member-management", this::renderMemberManagement);

        /** API */
        get("/score/:gameId", this::getGameScore);
        post("/member", this::addMember);
        post("/move/:gameId", this::movePiece);
        post("/terminate/:gameId", this::terminateGame);
        post("/chessGame", this::createGame);
        delete("/member/:memberId", this::deleteMember);
    }

    private String renderHome(final Request req, final Response res) {
        final Map<String, Object> model = new HashMap<>();
        final List<ChessGame> games = gameService.findPlayingGames();
        final List<Member> members = memberService.findAllMembers();
        model.put("games", games);
        model.put("members", members);
        return render(model, "index.html");
    }

    private String renderPlayGame(final Request req, final Response res) {
        final Long gameId = Long.valueOf(req.params("gameId"));
        final ChessGame chessGame = gameService.findByGameId(gameId);
        if (chessGame.isEnd()) {
            res.redirect(String.format("/result/%d", gameId));
        }

        final Map<String, Object> model = new HashMap<>();
        model.put("turn", chessGame.getTurn());
        model.put("ranks", makeRanksDto(chessGame));
        model.put("gameId", gameId);
        return render(model, "play.html");
    }

    private List<RankDto> makeRanksDto(final ChessGame chessGame) {
        final List<RankDto> ranks = new ArrayList<>();
        for (int i = 8; i > 0; i--) {
            final List<Piece> pieces = chessGame.getBoard().getPiecesByRank(Rank.from(i));
            ranks.add(RankDto.toDto(pieces, i));
        }
        return ranks;
    }

    private String renderGameResult(final Request req, final Response res) {
        final Long gameId = Long.valueOf(req.params("gameId"));
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        final Map<String, Object> model = new HashMap<>();
        model.put("winner", result.getWinner().name());
        model.put("whiteScore", result.getWhiteScore());
        model.put("blackScore", result.getBlackScore());
        return render(model, "result.html");
    }

    private String renderMemberHistory(final Request req, final Response res) {
        final Long memberId = Long.valueOf(req.params("memberId"));
        final List<GameResultDto> history = gameService.findHistoriesByMemberId(memberId);
        final Map<String, Object> model = new HashMap<>();
        model.put("history", history);
        return render(model, "history.html");
    }

    private String renderMemberManagement(final Request req, final Response res) {
        final List<Member> members = memberService.findAllMembers();
        final Map<String, Object> model = new HashMap<>();
        model.put("members", members);
        return render(model, "member-management.html");
    }

    private String getGameScore(final Request req, final Response res) {
        final Long gameId = Long.valueOf(req.params("gameId"));
        final ChessGame chessGame = gameService.findByGameId(gameId);
        final Result result = chessGame.createResult();

        final Map<String, String> jsonData = new HashMap<>();
        jsonData.put("whiteScore", String.valueOf(result.getWhiteScore()));
        jsonData.put("blackScore", String.valueOf(result.getBlackScore()));
        res.header("Content-Type", "application/json");
        res.body(JsonUtil.serialize(jsonData));

        return JsonUtil.serialize(jsonData);
    }

    private String addMember(final Request req, final Response res) {
        final String memberName = req.body();
        memberService.addMember(memberName);
        return "OK";
    }

    private ResponseDto movePiece(final Request req, final Response res) {
        final Long gameId = Long.valueOf(req.params("gameId"));
        final String[] positions = req.body().split(",");
        try {
            gameService.move(gameId, positions[0], positions[1]);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ResponseDto(400, e.getMessage());
        }
        return new ResponseDto(200, "");
    }

    private String terminateGame(final Request req, final Response res) {
        final Long gameId = Long.valueOf(req.params("gameId"));
        gameService.terminate(gameId);
        return "OK";
    }

    private String createGame(final Request req, final Response res) {
        final String body = req.body();
        final String[] ids = body.split(",");
        final Long whiteId = Long.valueOf(ids[0]);
        final Long blackId = Long.valueOf(ids[1]);
        gameService.createGame(whiteId, blackId);
        return "OK";
    }

    private String deleteMember(final Request req, final Response res) {
        final Long memberId = Long.valueOf(req.params("memberId"));
        memberService.deleteMember(memberId);
        return "OK";
    }
}
