package chess.dto;

public class SectionDTO {
    private String roomId;
    private String clickedSection;

    public SectionDTO() {
    }

    public SectionDTO(String roomId, String clickedSection) {
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
