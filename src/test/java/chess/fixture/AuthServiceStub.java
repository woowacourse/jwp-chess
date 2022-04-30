package chess.fixture;

import chess.service.AuthService;

public class AuthServiceStub extends AuthService {

    public AuthServiceStub() {
        super(new GameDaoStub());
    }
}
