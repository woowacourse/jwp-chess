package chess.service.dto;

import chess.domain.board.Board;

import java.util.List;
import java.util.stream.Collectors;

public class TilesDto {

    private List<TileDto> tiles;

    public TilesDto() {
    }

    public TilesDto(final Board board) {
        this.tiles = board.chessBoard()
                .values()
                .stream()
                .map(TileDto::new)
                .collect(Collectors.toList());
    }

    public List<TileDto> getTiles() {
        return tiles;
    }
}
