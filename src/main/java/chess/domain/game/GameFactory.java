package chess.domain.game;

import chess.dao.dto.GameDto;
import chess.dao.dto.PieceDto;
import chess.dao.dto.RoomDto;
import chess.domain.game.board.Board;
import chess.domain.game.board.piece.Piece;
import chess.domain.game.room.Room;
import chess.domain.game.team.Team;
import chess.domain.game.utils.PieceConverter;
import java.util.List;
import java.util.stream.Collectors;

public class GameFactory {

    public static Game of(final GameDto gameDto, final List<PieceDto> pieceDtos,
        final RoomDto roomDto) {

        final Board board = generateBoard(pieceDtos);
        final Room room = roomDto.toEntity();
        return composeGame(gameDto, board, room);
    }

    private static Game composeGame(final GameDto gameDto, final Board board, final Room room) {
        return Game.of(
            gameDto.getId(),
            gameDto.getCreatedTime(),
            Team.from(gameDto.getTurn()),
            gameDto.isFinished(),
            board,
            room
        );
    }

    private static Board generateBoard(List<PieceDto> pieceDtos) {
        final List<Piece> pieces = pieceDtos.stream()
            .map(PieceConverter::run)
            .collect(Collectors.toList());

        return Board.of(pieces);
    }

}
