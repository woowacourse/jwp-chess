package chess.domain.game;

import chess.dao.dto.GameDto;
import chess.dao.dto.PieceDto;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.team.Team;
import chess.utils.PieceConverter;
import java.util.List;
import java.util.stream.Collectors;

public class GameFactory {

    public static Game of(final GameDto gameDto, final List<PieceDto> pieceDtos) {
        final Board board = generateBoard(pieceDtos);
        return composeGame(gameDto, board);
    }

    private static Game composeGame(final GameDto gameDto, final Board board) {
        return new Game(
            gameDto.getId(),
            gameDto.getName(),
            Team.from(gameDto.getTurn()),
            gameDto.isFinished(),
            gameDto.getCreatedTime(),
            board
        );
    }

    private static Board generateBoard(List<PieceDto> pieceDtos) {
        final List<Piece> pieces = pieceDtos.stream()
            .map(PieceConverter::run)
            .collect(Collectors.toList());

        return Board.of(pieces);
    }

}
