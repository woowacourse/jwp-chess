package chess.model.dto;

public class PromotionTypeDto {

    private final String promotionType;
    private final Integer gameId;

    public PromotionTypeDto(String promotionType, Integer gameId) {
        this.promotionType = promotionType;
        this.gameId = gameId;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public Integer getGameId() {
        return gameId;
    }
}
