package chess.web.dto.game.join;

public class JoinRequestDto {

    private long guestId;

    public JoinRequestDto() {
    }

    public JoinRequestDto(final long guestId) {
        this.guestId = guestId;
    }

    public long getGuestId() {
        return guestId;
    }

}
