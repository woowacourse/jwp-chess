package chess.domain.state;

import chess.domain.exception.InvalidStateException;

public class InitialState implements State {
    public InitialState() {
        super();
    }

    @Override
    public State start() {
        return new Running();
    }

    @Override
    public State status() {
        throw new InvalidStateException("통계를 보여줄 수 없습니다. - 진행했거나 진행한 게임이 없습니다.");
    }

    @Override
    public State move(boolean isKingDead) {
        throw new InvalidStateException("이동 명령을 수행할 수 없습니다. - 진행중인 게임이 없습니다.");
    }

    @Override
    public boolean isNotRunning() {
        return true;
    }
}
