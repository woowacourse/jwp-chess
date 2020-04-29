package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wooteco.chess.domain.Scores;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.service.BoardService;
import wooteco.chess.service.RoomService;

import java.sql.SQLException;
import java.util.Map;

@Controller
public class SpringChessController {
    private final BoardService boardService;
    private final RoomService roomService;

    public SpringChessController(BoardService boardService, RoomService roomService) {
        this.boardService = boardService;
        this.roomService = roomService;
    }

    @GetMapping("/")
    public String index(Model model) throws SQLException {
        model.addAttribute("roomNumbers", roomService.loadRoomNumbers());
        return "index";
    }

    @PostMapping("/newroom")
    public String newRoom() throws SQLException {
        int roomId = roomService.create();
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/rooms/{roomId}")
    public String loadBoard(@PathVariable int roomId, Model model) throws SQLException {
        constructModel(roomId, model);
        return "board";
    }

    @PostMapping("/rooms/{roomId}")
    public String move(@PathVariable int roomId, @RequestParam String source, @RequestParam String target, Model model) throws SQLException {
        try {
            boardService.play(roomId, source, target);
        } catch (RuntimeException e) {
            model.addAttribute("error-message", e.getMessage());
            constructModel(roomId, model);
            return "board";
        }
        if (boardService.isFinished(roomId)) {
            model.addAttribute("winner", boardService.isTurnWhite(roomId) ? "흑팀" : "백팀");
            return "result";
        }
        constructModel(roomId, model);
        return "board";
    }

    @PostMapping("/newgame/{roomId}")
    public String newGame(@PathVariable int roomId) throws SQLException{
        boardService.init(roomId);
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/scores/{roomId}")
    public String scores(@PathVariable int roomId, Model model) throws SQLException {
        model.addAttribute("id", roomId);
        model.addAttribute("scores", Scores.calculateScores(boardService.getBoard(roomId)));
        return "scores";
    }









    private void constructModel(int roomId, Model model) throws SQLException {
        BoardDto boardDTO = new BoardDto(boardService.getBoard(roomId));
        Map<String, String> pieces = boardDTO.getBoard();
        for (String positionKey : pieces.keySet()) {
            String imageName = pieces.get(positionKey);
            if (imageName.equals(".")) {
                imageName = "blank";
            }
            model.addAttribute(positionKey, imageName);
            model.addAttribute("id", roomId);
        }
    }
}
