package chess.service.dto;

import chess.entity.Chess;

import java.util.List;
import java.util.stream.Collectors;

public class ChessInfosDto {
    private List<ChessInfoDto> chessInfos;

    public ChessInfosDto() {
    }

    public ChessInfosDto(List<Chess> chessInfos) {
        this.chessInfos = chessInfos.stream()
                .map(Chess::getName)
                .map(ChessInfoDto::new)
                .collect(Collectors.toList());
    }

    public List<ChessInfoDto> getChessInfos() {
        return chessInfos;
    }

    public void setChessInfos(List<ChessInfoDto> chessInfos) {
        this.chessInfos = chessInfos;
    }
}
