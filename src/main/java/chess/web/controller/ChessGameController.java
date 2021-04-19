package chess.web.controller;


import chess.web.controller.dto.request.CreateGameRequestDto;
import chess.web.controller.dto.response.ChessGameResponseDto;
import chess.web.controller.dto.response.CreateGameResponseDto;
import chess.web.controller.dto.response.GameStatusResponseDto;
import chess.web.service.ChessGameService;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ChessGameController {

    private static final String ENCRYPTED_WHITE_PLAYER_PASSWORD = "encryptedWhitePlayerPassword";
    private static final String ENCRYPTED_BLACK_PLAYER_PASSWORD = "encryptedBlackPlayerPassword";

    private final ChessGameService chessGameService;

    public ChessGameController(ChessGameService chessGameService) {
        this.chessGameService = chessGameService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<ChessGameResponseDto> notFulledChessGames = chessGameService.getNotFulledChessGames();
        model.addAttribute("notFulledChessGames", notFulledChessGames);
        return "index";
    }

    @PostMapping("/games")
    public String createChessGame(@ModelAttribute CreateGameRequestDto createGameRequestDto, HttpServletResponse response) {
        CreateGameResponseDto createGameResponseDto = chessGameService.createNewChessGame(createGameRequestDto);
        Cookie cookie = new Cookie(ENCRYPTED_WHITE_PLAYER_PASSWORD, createGameResponseDto.getEncryptedWhitePlayerPassword());
        response.addCookie(cookie);
        return "redirect:/games/" + createGameResponseDto.getGameId();
    }

    @GetMapping("/games/{gameId}")
    public String chessBoard(@PathVariable Long gameId, Model model) {
        GameStatusResponseDto gameStatusDto = chessGameService.getGameStatus(gameId);
        model.addAttribute("gameStatusDto", gameStatusDto);
        return "chess-game";
    }
}
