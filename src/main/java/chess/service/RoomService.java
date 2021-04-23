package chess.service;

import chess.dao.RoomDao;
import chess.dto.RoomDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RoomService {
    private final RoomDao roomDao;

    @Transactional(readOnly = true)
    public List<RoomDto> findAllDesc() {
        return roomDao.getRooms().stream()
            .map(room -> new RoomDto(room.getId(), room.getName()))
            .collect(Collectors.toList());
    }

    public void delete(int id) {
        roomDao.delete(id);
    }

    public void add(String name) {
        roomDao.insert(name);
    }
}
