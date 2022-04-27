package chess.dto;

public class LobbyGameDto {
    private final Long id;
    private final String title;
    private final String whiteName;
    private final String blackName;
    private final String inProgress;
    private final String progressBadge;

    public LobbyGameDto(final Long id,
                        final String title,
                        final String whiteName,
                        final String blackName,
                        final boolean isEnd) {
        this.id = id;
        this.title = title;
        this.whiteName = whiteName;
        this.blackName = blackName;
        this.inProgress = createInProgress(isEnd);
        this.progressBadge = createProgressBadge(isEnd);
    }

    private String createInProgress(final boolean isEnd) {
        if(isEnd) {
            return "게임 종료";
        }
        return "게임 진행 중";
    }

    private String createProgressBadge(final boolean isEnd) {
        if(isEnd) {
            return "badge bg-danger";
        }
        return "badge bg-success";
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getInProgress() {
        return inProgress;
    }

    public String getProgressBadge() {
        return progressBadge;
    }
}
