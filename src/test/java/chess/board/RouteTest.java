package chess.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spring.chess.board.Route;
import spring.chess.location.Location;
import spring.chess.piece.type.Pawn;
import spring.chess.piece.type.Piece;
import spring.chess.team.Team;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RouteTest {

    @Test
    @DisplayName("좌표가 비어있지 않을 경우")
    void isNotEmpty() {
        Map<Location, Piece> route = new HashMap<>();
        Location location = new Location(2, 'b');

        route.put(location, new Pawn(Team.WHITE));

        Route route1 = new Route(route, new Location(1, 'b'), new Location(3, 'b'));
        assertThat(route1.isExistPieceIn(location)).isTrue();
    }

    @Test
    @DisplayName("좌표가 비어있는 경우")
    void isNotEmpty2() {
        Map<Location, Piece> route = new HashMap<>();
        Location location = new Location(2, 'b');

        Route route1 = new Route(route, new Location(1, 'b'), new Location(3, 'b'));
        assertThat(route1.isExistPieceIn(location)).isFalse();
    }
}