package chess.dto;

public class PromotionTypeDto {

    private String promotionType;

    public PromotionTypeDto() {
    }

    public PromotionTypeDto(String promotionType) {
        this.promotionType = promotionType;
    }

    public String getPromotionType() {
        return promotionType;
    }
}
