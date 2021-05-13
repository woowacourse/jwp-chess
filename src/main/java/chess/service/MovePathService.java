package chess.service;

import chess.domain.board.piece.Owner;
import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.manager.Game;
import chess.domain.repository.game.GameRepository;
import chess.domain.room.Room;
import chess.domain.user.User;
import chess.exception.PasswordMissMatchException;
import chess.exception.TurnOwnerException;
import chess.service.dto.path.PathDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovePathService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final RoomService roomService;

    public MovePathService(GameRepository gameRepository, UserService userService, RoomService roomService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    @Transactional(readOnly = true)
    public PathDto movablePath(final String source, final Long gameId, final String password) {
        Game game = gameRepository.findById(gameId);
        try {
            checkTurnOwner(gameId, password, game.turnOwner());
        } catch (PasswordMissMatchException e) {
            throw new TurnOwnerException("상대방의 차례입니다!!");
        }
        Path path = game.movablePath(Position.of(source));
        return PathDto.from(path);
    }

    private void checkTurnOwner(final Long gameId, final String password, final Owner owner) {
        Room room = roomService.findByGameId(gameId);
        if (owner.isSame(Owner.WHITE)) {
            User whiteUser = userService.findById(room.whiteUserId());
            whiteUser.checkPassword(password);
            return;
        }
        if (owner.isSame(Owner.BLACK)) {
            User blackUser = userService.findById(room.blackUserId());
            blackUser.checkPassword(password);
        }
    }
}
