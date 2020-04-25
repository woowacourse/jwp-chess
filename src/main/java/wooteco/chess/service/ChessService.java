package wooteco.chess.service;

import wooteco.chess.consolView.PieceRender;
import wooteco.chess.domain.Chess;
import wooteco.chess.domain.board.BoardGenerator;
import wooteco.chess.domain.board.Tile;
import wooteco.chess.domain.coordinate.Coordinate;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.dto.ChessResponseDto;
import wooteco.chess.dto.MoveDto;
import wooteco.chess.repository.MoveRepository;
import wooteco.chess.repository.MoveRepositoryImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChessService {
    private MoveRepository moveRepository = new MoveRepositoryImpl();
    private Map<Integer, Chess> cachedChess = new HashMap<>();

    public wooteco.chess.result.Result move(MoveDto moveDto) {
        if (!cachedChess.containsKey(moveDto.getRoomId())) {
            return new wooteco.chess.result.Result(false, "Can not find room. room id : " + moveDto.getRoomId());
        }
        Chess chess = cachedChess.get(moveDto.getRoomId());
        chess.move(moveDto.getSource(), moveDto.getTarget());
        if (!chess.isKingAlive()) {
            return new wooteco.chess.result.Result(true, "win");
        }
        try {
            return moveRepository.add(moveDto);
        } catch (SQLException e) {
            return new wooteco.chess.result.Result(false, "SQLException occur.");
        }
    }

    public wooteco.chess.result.Result getMovableWay(int roomId, Team team, Coordinate coordinate) {
        if (!cachedChess.containsKey(roomId)) {
            return new wooteco.chess.result.Result(false, "Can not find room. room id : " + roomId);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isTurnOf(team) || chess.isTurnOf(coordinate)) {
            return new wooteco.chess.result.Result(false, "not your turn");
        }

        List<String> movableWay = chess.getMovableWay(coordinate);
        return new wooteco.chess.result.Result(true, movableWay);
    }

    public wooteco.chess.result.Result renew(int roomId) {
        if (!cachedChess.containsKey(roomId)) {
            load(roomId);
        }
        Chess chess = cachedChess.get(roomId);
        if (!chess.isKingAlive()) {
            return new wooteco.chess.result.Result(true, "lose");
        }
        return new wooteco.chess.result.Result(true, ChessResponseDto.of(chess));
    }

    private void load(int roomId) {
        Chess chess = new Chess(BoardGenerator.create());
        try {
            wooteco.chess.result.Result result = moveRepository.findByRoomId(roomId);
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
