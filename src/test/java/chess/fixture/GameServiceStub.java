package chess.fixture;

import chess.service.GameService;

public class GameServiceStub extends GameService {

    public GameServiceStub() {
        super(new GameDaoStub(), new EventDaoStub());
    }
}
