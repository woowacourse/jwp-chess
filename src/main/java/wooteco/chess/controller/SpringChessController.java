package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import wooteco.chess.domain.Scores;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.service.BoardService;
import wooteco.chess.service.RoomService;

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
    public String index(Model model) {
        System.out.println("첫화면");
        model.addAttribute("roomNumbers", roomService.loadRoomNumbers());
        return "index";
    }

    @PostMapping("/newroom")
    public String newRoom() {
        System.out.println("새 방");
        Long roomId = roomService.create();
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/rooms/{roomId}")
    public String loadBoard(@PathVariable Long roomId, Model model) {
        constructModel(roomId, model);
        return "board";
    }

//    @PostMapping("/rooms/{roomId}")
//    public String move(@PathVariable Long roomId, @RequestParam String source, @RequestParam String target, Model model) throws SQLException {
//        try {
//            boardService.play(roomId, source, target);
//        } catch (RuntimeException e) {
//            model.addAttribute("error-message", e.getMessage());
//            constructModel(roomId, model);
//            return "board";
//        }
//        if (boardService.isFinished(roomId)) {
//            model.addAttribute("winner", boardService.isTurnWhite(roomId) ? "흑팀" : "백팀");
//            return "result";
//        }
//        constructModel(roomId, model);
//        return "board";
//    }

    @PostMapping("/newgame/{roomId}")
    public String newGame(@PathVariable Long roomId) {
        boardService.init(roomId);
        return "redirect:/rooms/" + roomId;
    }

    @GetMapping("/scores/{roomId}")
    public String scores(@PathVariable Long roomId, Model model) {
        model.addAttribute("id", roomId);
        model.addAttribute("scores", Scores.calculateScores(boardService.loadBoard(roomId)));
        return "scores";
    }

    private void constructModel(Long roomId, Model model) {
        BoardDto boardDTO = new BoardDto(boardService.loadBoard(roomId));
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
