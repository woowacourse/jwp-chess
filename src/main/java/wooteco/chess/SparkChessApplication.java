package wooteco.chess;

import wooteco.chess.controller.spark.SparkController;

public class SparkChessApplication {
	public static void main(String[] args) {
		new SparkController().route();
	}
}
