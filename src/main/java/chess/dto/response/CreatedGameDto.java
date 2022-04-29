package chess.dto.response;

import chess.util.CookieUtil;
import java.util.Objects;
import javax.servlet.http.Cookie;

public class CreatedGameDto {

    private final int gameId;
    private final Cookie cookie;

    private CreatedGameDto(int gameId, Cookie cookie) {
        this.gameId = gameId;
        this.cookie = cookie;
    }

    public static CreatedGameDto of(int gameId) {
        Cookie cookie = CookieUtil.generateGameOwnerCookie(gameId);
        return new CreatedGameDto(gameId, cookie);
    }

    public int getGameId() {
        return gameId;
    }

    public Cookie getCookie() {
        return cookie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreatedGameDto that = (CreatedGameDto) o;
        return gameId == that.gameId
                && cookie.getValue().equals(that.cookie.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, cookie);
    }

    @Override
    public String toString() {
        return "CreatedGameDto{" +
                "gameId=" + gameId +
                ", cookie=" + cookie +
                '}';
    }
}
