package chess.domain.password;

import java.util.Objects;
import java.util.Optional;

public class WhitePassword implements Password {
    private final String password;

    public WhitePassword(String password) {
        Objects.requireNonNull(password);
        this.password = password;
    }

    @Override
    public Optional<String> get() {
        return Optional.of(password);
    }

    @Override
    public boolean isSame(String userPassword) {
        return password.equals(userPassword);
    }
}
