package chess.dto;

public final class SectionDTO {

    private String roomId;
    private String clickedSection;

    public SectionDTO() {
    }

    public SectionDTO(final String roomId, final String clickedSection) {
        this.roomId = roomId;
        this.clickedSection = clickedSection;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getClickedSection() {
        return clickedSection;
    }
}
