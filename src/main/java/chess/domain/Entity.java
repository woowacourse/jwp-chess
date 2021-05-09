package chess.domain;

public class Entity<E> {
    protected E id;

    public Entity() {
    }

    public Entity(E id) {
        this.id = id;
    }

    public E getId() {
        return this.id;
    };
}
