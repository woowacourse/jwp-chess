package chess.controller;

import chess.dto.ChessGameInfoResponseDto;
import chess.dto.PlayingChessgameEntityDto;
import chess.service.ChessGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChessController {

    private final ChessGameService chessGameService;

    public ChessController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String chessgamesList(Model model) {
        List<PlayingChessgameEntityDto> chessGameDtos = chessGameService.findAllPlayingGames();
        model.addAttribute("chessGameDtos", chessGameDtos);
        return "index";
    }

    @GetMapping("/chessgames/{id}")
    public String chessGame(@PathVariable("id") Long id, Model model) {
        ChessGameInfoResponseDto chessGameInfoResponseDto = chessGameService.findChessGameInfoById(id);
        model.addAttribute("chessGameInfoResponseDto", chessGameInfoResponseDto);
        return "chessgame-room";
    }

}
