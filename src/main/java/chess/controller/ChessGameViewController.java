package chess.controller;

import chess.dto.MakeRoomDto;
import chess.dto.PasswordDto;
import chess.service.ChessService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class ChessGameViewController {

    private final ChessService chessService;

    public ChessGameViewController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping("/")
    public String returnHomeView() {
        return "index";
    }

    @GetMapping("/create")
    public String returnRoomView() {
        return "room";
    }

    @GetMapping("/game-list")
    public String returnGameListView() {
        return "gamelist";
    }

    @GetMapping("/board/{id}")
    public String returnBoardView() {
        return "board";
    }

    @PostMapping("/room")
    public String createRoom(MakeRoomDto makeRoomDto) {
        Long id = chessService.makeGame(makeRoomDto);
        return "redirect:/board/" + id;
    }

    @PostMapping("/participate/{id}")
    public String participateRoom(@PathVariable Long id) {
        chessService.loadExistGame(id);
        return "redirect:/board/" + id;
    }

    @PostMapping("/{id}")
    public String deleteGame(@PathVariable Long id, PasswordDto passwordDto, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if (chessService.isPossibleDeleteGame(id, passwordDto.getPassword())) {
            chessService.endGame(id);
            out.println("<script>alert('체스가 삭제되었습니다.'); location.href='/game-list';</script>");
            out.flush();
        }
        out.println("<script>alert('체스를 종료하고, 올바른 비밀번호를 눌러주세요.'); location.href='/game-list';</script>");
        out.flush();
        return "redirect:/game-list";
    }
}
