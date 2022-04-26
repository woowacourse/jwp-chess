package chess.domain;

public class Room {
    private final String title;
    private final String password;
    private final Participant participant;

    public Room(final String title, final String password, final Participant participant) {
        this.title = title;
        this.password = password;
        this.participant = participant;
    }

    public String getTitle() {
        return title;
    }

    public String getPassword() {
        return password;
    }

    public Participant getParticipant() {
        return participant;
    }
}
