package chess.domain.password;

import java.util.Optional;

public interface Password {
    Optional<String> get();

    boolean isSame(String password);
}
