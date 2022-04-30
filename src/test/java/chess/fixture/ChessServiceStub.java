package chess.fixture;

import chess.service.ChessService;

public class ChessServiceStub extends ChessService {

    public ChessServiceStub() {
        super(new GameDaoStub(), new EventDaoStub());
    }
}
