package chess;

import chess.domain.game.BoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.domain.position.Position;
import chess.dto.RequestDto;
import chess.dto.ResponseDto;
import chess.dto.StatusDto;
import chess.mapper.Command;
import chess.service.GameService;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class SpringChessApplication {

    private static final String MOVE_DELIMITER = " ";
    private static final int MOVE_COMMAND_SIZE = 3;
    private static final int SOURCE_INDEX = 1;
    private static final int TARGET_INDEX = 2;

    private final GameService gameService;

    public SpringChessApplication(GameService gameService) {
        this.gameService = gameService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringChessApplication.class, args);
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("boards", gameService.getRooms());
        return "home";
    }

    @PostMapping("/room")
    public String createRoom(@ModelAttribute RequestDto requestDto) {
        final ChessBoard board = new ChessBoard(requestDto.getTitle(), Color.WHITE,
                List.of(new Member(requestDto.getFirstMemberName()), new Member(requestDto.getSecondMemberName())));
        int roomId = gameService.saveBoard(board, new BoardInitializer()).getId();
        return "redirect:/room/" + roomId;
    }

    @GetMapping("/room/{roomId}")
    public String showRoom(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("roomId", id);
        model.addAttribute("board", gameService.getBoard(id));
        return "index";
    }

    @ResponseBody
    @PostMapping("/room/{roomId}/move")
    public ResponseDto movePiece(@PathVariable("roomId") int id, @RequestBody String body) {
        final String[] split = body.split("=");
        if (Command.isMove(split[1])) {
            return getResponseDto(id, split[1]);
        }
        return new ResponseDto(HttpStatus.BAD_REQUEST_400, "잘못된 명령어 입니다.", gameService.isEnd(id));
    }

    private ResponseDto getResponseDto(int roomId, String command) {
        try {
            move(roomId, Arrays.asList(command.split(MOVE_DELIMITER)));
        } catch (IllegalArgumentException e) {
            return new ResponseDto(HttpStatus.BAD_REQUEST_400, e.getMessage(), gameService.isEnd(roomId));
        }
        return new ResponseDto(HttpStatus.OK_200, "", gameService.isEnd(roomId));
    }

    private void move(int roomId, final List<String> commands) {
        if (commands.size() == MOVE_COMMAND_SIZE) {
            gameService.move(roomId, Position.of(commands.get(SOURCE_INDEX)), Position.of(commands.get(TARGET_INDEX)));
        }
    }

    @PostMapping("/room/{roomId}/end")
    public String endGame(@PathVariable("roomId") int id, Model model) {
        model.addAttribute("result", gameService.status(id));
        gameService.end(id);
        return "result";
    }

    @ResponseBody
    @GetMapping("/room/{roomId}/status")
    public StatusDto showStatus(@PathVariable("roomId") int id) {
        return gameService.status(id);
    }
}
