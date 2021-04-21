package chess.web.controller;

import static chess.web.view.RenderView.renderHtml;

import chess.domain.game.ChessGame;
import chess.domain.game.dao.ChessGameDao;
import chess.service.ChessService;
import chess.domain.board.dto.BoardDto;
import chess.domain.game.dto.ChessGameDto;
import chess.domain.game.dto.GameNameDto;
import chess.domain.command.dto.MoveCommandDto;
import java.util.HashMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chess")
public class ChessController {

    private final ChessGameDao chessGameDao;
    private final ChessService chessService;

    public ChessController(ChessGameDao chessGameDao, ChessService chessService) {
        this.chessGameDao = chessGameDao;
        this.chessService = chessService;
    }

    @PostMapping
    @ResponseBody
    public Object createGameView(@RequestBody GameNameDto gameNameDto) {
        return chessService.addChessGame(gameNameDto.getGameName());
    }

    @GetMapping("/{gameId}")
    public String gameDetailView(@PathVariable String gameId, Model model) {
        ChessGameDto chessGameDTO = chessGameDao.findGameById(gameId);

        model.addAttribute("gameId", chessGameDTO.getId());
        model.addAttribute("gameName", chessGameDTO.getName());
        return "game";
    }

    @GetMapping(value = "/{gameId}/board")
    @ResponseBody
    public Object boardDetailApi(@PathVariable String gameId) {
        ChessGame chessGame = chessService.replayedChessGame(gameId);

        // todo renderHtml Spark 의존 없애기
        String boardHtml = renderHtml(new BoardDto(chessGame).getResult(), "/board.hbs");
        HashMap<String, Object> model = new HashMap<>();
        model.put("board", boardHtml);
        model.put("currentTurn", chessGame.currentTurn());

        return model;
    }

    @PostMapping(value = "/{gameId}/move")
    @ResponseBody
    public Object movePieceApi(@PathVariable String gameId, @RequestBody MoveCommandDto moveDto) {
        return chessService.movePiece(gameId, moveDto);
    }
}
