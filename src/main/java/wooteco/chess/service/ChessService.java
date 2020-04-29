package wooteco.chess.service;

import org.springframework.stereotype.Service;
import wooteco.chess.domain.Chess;
import wooteco.chess.domain.board.BoardGenerator;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.dto.ResponseDto;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.support.ChessResponseCode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private MoveRepository moveRepository;
    private Map<Integer, Chess> cachedChess = new HashMap<>();

    public ChessService(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    public ResponseDto move(MoveDto moveDto) throws SQLException {
        if (!cachedChess.containsKey(moveDto.getRoomId())) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }

        Chess chess = cachedChess.get(moveDto.getRoomId());
        chess.move(moveDto.getSource(), moveDto.getTarget());
        moveRepository.add(moveDto);

        if (!chess.isKingAlive()) {
            return ResponseDto.success(ChessResponseCode.WIN);
        }
        return ResponseDto.success();
    }

    public ResponseDto getMovableWay(int roomId, Team team, Coordinate coordinate) {
        if (!cachedChess.containsKey(roomId)) {
            return ResponseDto.fail(ChessResponseCode.CANNOT_FIND_ROOM_ID);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isTurnOf(team) || chess.isTurnOf(coordinate)) {
            return ResponseDto.fail(ChessResponseCode.NOT_YOUR_TURN);
        }
        return ResponseDto.success(chess.getMovableWay(coordinate));
    }

    public ResponseDto renew(int roomId) throws SQLException{
        if (!cachedChess.containsKey(roomId)) {
            load(roomId);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isKingAlive()) {
            return ResponseDto.success(ChessResponseCode.LOSE);
        }
        return ResponseDto.success(ChessResponseDto.of(chess));
    }

    private void load(int roomId) throws SQLException{
        Chess chess = new Chess(BoardGenerator.create());
        List<MoveDto> moveDtos = moveRepository.findByRoomId(roomId);
        for (MoveDto moveDto : moveDtos) {
            chess.move(moveDto.getSource(), moveDto.getTarget());
        }
        cachedChess.put(roomId, chess);
    }
}
