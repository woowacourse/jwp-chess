package chess.service;

import chess.dao.GameDao;
import chess.domain.auth.EncryptedAuthCredentials;
import chess.domain.auth.PlayerCookie;
import chess.domain.board.piece.Color;
import chess.dto.response.EnterGameDto;
import chess.entity.FullGameEntity;
import chess.util.CookieUtil;
import javax.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final GameDao gameDao;

    public AuthService(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    public Cookie generateCreatedGameCookie(int gameId) {
        return CookieUtil.generateGameOwnerCookie(gameId);
    }

    @Transactional
    public EnterGameDto loginOrSignUpAsOpponent(int gameId, EncryptedAuthCredentials authCredentials) {
        FullGameEntity game = gameDao.findFullDataById(gameId);
        if (game.isOwnedBy(authCredentials)) {
            return EnterGameDto.ofOwner(gameId);
        }
        return signUpAsOpponent(authCredentials, game);
    }

    private EnterGameDto signUpAsOpponent(EncryptedAuthCredentials authCredentials, FullGameEntity game) {
        int gameId = game.getId();
        if (!game.hasOpponent()) {
            return saveValidOpponent(gameId, authCredentials);
        }
        validateOpponent(authCredentials, game);
        return EnterGameDto.ofOpponent(gameId);
    }

    private EnterGameDto saveValidOpponent(int gameId, EncryptedAuthCredentials authCredentials) {
        gameDao.saveOpponent(authCredentials);
        return EnterGameDto.ofOpponent(gameId);
    }

    private void validateOpponent(EncryptedAuthCredentials authCredentials,
                                  FullGameEntity game) {
        if (!game.hasOpponentOf(authCredentials)) {
            throw new IllegalArgumentException("잘못된 비밀번호를 입력하였습니다.");
        }
    }

    public Color parseValidCookie(int gameId, PlayerCookie cookie) {
        return cookie.parsePlayerColorBy(gameId);
    }
}
