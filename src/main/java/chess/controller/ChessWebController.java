package chess.controller;

import chess.dao.JdbcRoomDao;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChessWebController {

    private final JdbcRoomDao jdbcRoomDao;

    public ChessWebController(JdbcRoomDao jdbcRoomDao) {
        this.jdbcRoomDao = jdbcRoomDao;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/rooms/{id}")
    public String room(@PathVariable final int id) {
        boolean roomExist = jdbcRoomDao.existsById(id);
        if (!roomExist) {
            return "redirect:/";
        }

        return "board";
    }
}
