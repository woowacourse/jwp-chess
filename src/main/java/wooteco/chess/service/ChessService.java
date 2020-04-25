package wooteco.chess.service;

import wooteco.chess.domain.Chess;
import wooteco.chess.domain.board.BoardGenerator;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.MoveRepositoryImpl;
import wooteco.chess.result.Result;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChessService {
    private MoveRepository moveRepository = new MoveRepositoryImpl();
    private Map<Integer, Chess> cachedChess = new HashMap<>();

    public Result move(MoveDto moveDto) {
        if (!cachedChess.containsKey(moveDto.getRoomId())) {
            return new Result(false, "Can not find room. room id : " + moveDto.getRoomId());
        }
        Chess chess = cachedChess.get(moveDto.getRoomId());
        chess.move(moveDto.getSource(), moveDto.getTarget());
        if (!chess.isKingAlive()) {
            return new Result(true, "win");
        }
        try {
            return moveRepository.add(moveDto);
        } catch (SQLException e) {
            return new Result(false, "SQLException occur.");
        }
    }

    public Result getMovableWay(int roomId, Team team, Coordinate coordinate) {
        if (!cachedChess.containsKey(roomId)) {
            return new Result(false, "Can not find room. room id : " + roomId);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isTurnOf(team) || chess.isTurnOf(coordinate)) {
            return new Result(false, "not your turn");
        }

        List<String> movableWay = chess.getMovableWay(coordinate);
        return new Result(true, movableWay);
    }

    public Result renew(int roomId) {
        if (!cachedChess.containsKey(roomId)) {
            load(roomId);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isKingAlive()) {
            return new Result(true, "lose");
        }
        return new Result(true, ChessResponseDto.of(chess));
    }

    private void load(int roomId) {
        Chess chess = new Chess(BoardGenerator.create());
        try {
            Result result = moveRepository.findByRoomId(roomId);
            if (Objects.isNull(result.getObject())) {
                cachedChess.put(roomId, chess);
                return;
            }
            List<MoveDto> moveDtos = (List<MoveDto>) result.getObject();
            for (MoveDto moveDto : moveDtos) {
                chess.move(moveDto.getSource(), moveDto.getTarget());
            }
            cachedChess.put(roomId, chess);
        } catch (SQLException e) {
            return;
        }
    }
}
