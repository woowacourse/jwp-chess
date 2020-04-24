package wooteco.chess.dao;

class SQLAccessException extends RuntimeException {
	private static final String DEFAULT_MESSAGE = "DB에 문제가 발생하였습니다.";

	SQLAccessException() {
		super(DEFAULT_MESSAGE);
	}

	SQLAccessException(String message) {
		super(message);
	}
}