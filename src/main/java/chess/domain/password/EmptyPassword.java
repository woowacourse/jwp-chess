package chess.domain.password;

import java.util.Optional;

public class EmptyPassword implements Password {

    @Override
    public Optional<String> get() {
        return Optional.empty();
    }

    @Override
    public boolean isSame(String userPassword) {
        return false;
    }

    @Override
    public boolean isEmptyPassword() {
        return true;
    }
}