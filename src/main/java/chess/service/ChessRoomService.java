package chess.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChessRoomService {

    @GetMapping
    public List<Void> showRooms(){

        return new ArrayList<>();
    }

    @PostMapping("/{roomId}")
    public void createNewRoom(){

    }
}
