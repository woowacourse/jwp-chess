package chess.domain;

public class RoomInfo {
    private final String title;
    private final String password;
    private final Participant participant;

    public RoomInfo(final String title, final String password, final Participant participant) {
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
