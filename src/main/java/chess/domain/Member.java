package chess.domain;

public class Member {
    private Long id;
    private final String name;

    public Member(final String name) {
        this.name = name;
    }

    public Member(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

