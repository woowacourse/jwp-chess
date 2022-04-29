package chess.entity;

import chess.domain.auth.EncryptedAuthCredentials;
import java.util.Objects;

public class FullGameEntity {

    private final int id;
    private final String name;
    private final String password;
    private final String opponentPassword;
    private final boolean running;

    public FullGameEntity(int id,
                          String name,
                          String password,
                          String opponentPassword,
                          boolean running) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.opponentPassword = opponentPassword;
        this.running = running;
    }

    public boolean hasNameOf(String name) {
        return this.name.equals(name);
    }

    public boolean isOwnedBy(EncryptedAuthCredentials authCredentials) {
        return hasNameOf(authCredentials.getName()) &&
                password.equals(authCredentials.getPassword());
    }

    public boolean hasOpponent() {
        return password != null;
    }

    public boolean hasOpponentOf(EncryptedAuthCredentials authCredentials) {
        return hasNameOf(authCredentials.getName()) &&
                opponentPassword.equals(authCredentials.getPassword());
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FullGameEntity that = (FullGameEntity) o;
        return id == that.id
                && running == that.running
                && Objects.equals(name, that.name)
                && Objects.equals(password, that.password)
                && Objects.equals(opponentPassword, that.opponentPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, opponentPassword, running);
    }

    @Override
    public String toString() {
        return "FullGameEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", opponentPassword='" + opponentPassword + '\'' +
                ", running=" + running +
                '}';
    }
}
