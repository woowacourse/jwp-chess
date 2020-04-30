package chess.dto.view;

public class PromotionTypeDto {

    private String promotionType;

    protected PromotionTypeDto() {
    }

    public String getPromotionType() {
        return promotionType;
    }

    @Override
    public String toString() {
        return "PromotionTypeDto{" +
            "promotionType='" + promotionType + '\'' +
            '}';
    }
}
