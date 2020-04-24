package wooteco.chess.controller;

import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import wooteco.chess.dao.ChessGameDao;

@Controller
public class WebChessGameController {
    private static final ChessGameDao chessGameDao = new ChessGameDao();

    @GetMapping("/game/{id}")
    public String renderGamePage(@PathVariable String id, Model model) throws SQLException {
        if (chessGameDao.selectAll().contains(Integer.parseInt(id))) {
            model.addAttribute("id", id);
            return "game";
        }
        return "<script>location.replace('/')</script>";
    }

    @GetMapping("/")
    public String renderIndexPage() {
        return "index";
    }

}
