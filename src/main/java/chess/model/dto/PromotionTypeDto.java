package chess.model.dto;

public class PromotionTypeDto {

    private String promotionType;
    private Integer gameId;

    public PromotionTypeDto() {
    }

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
