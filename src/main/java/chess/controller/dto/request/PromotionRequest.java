package chess.controller.dto.request;

import chess.domain.PromotionPiece;
import javax.validation.constraints.NotNull;

public class PromotionRequest {

	@NotNull(message = "프로모션할 기물을 입력해 주세요.")
	private String promotionValue;

	private PromotionRequest() {
	}

	public PromotionRequest(String promotionValue) {
		this.promotionValue = promotionValue;
	}

	public String getPromotionValue() {
		return promotionValue;
	}

	public PromotionPiece toPromotionPiece() {
		return PromotionPiece.createPromotionPiece(promotionValue);
	}
}
