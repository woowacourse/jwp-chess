package chess.service;

import chess.domain.piece.Color;
import chess.dto.GameCreateRequest;
import chess.dto.GameCreateResponse;
import chess.dto.GameDeleteRequest;
import chess.dto.GameDeleteResponse;
import chess.dto.GameDto;
import chess.dto.MoveRequest;
import chess.dto.MoveResponse;
import chess.dto.PositionDto;
import java.util.List;
import java.util.Map;

public interface ChessService {

    GameCreateResponse create(GameCreateRequest gameCreateRequest);

    List<GameDto> findAll();

    GameDto findById(long id);

    Map<Color, Double> findScoreById(long gameId);

    List<PositionDto> findPositionsById(long gameId);

    MoveResponse updateBoard(long id, MoveRequest moveRequest);

    GameDeleteResponse deleteById(GameDeleteRequest gameDeleteRequest);

    long deleteAll();
}
