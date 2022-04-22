package chess.controller.dto;

import chess.domain.PromotionPiece;

public class PromotionRequest {

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
