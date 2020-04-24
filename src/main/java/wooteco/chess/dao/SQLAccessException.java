package wooteco.chess.dao;

class SQLAccessException extends RuntimeException {
	static final String UPDATE_FAIL = " 테이블 UPDATE에 실패하였습니다.";
	static final String SAVE_FAIL = " 테이블 SAVE에 실패하였습니다.";
	static final String DELETE_FAIL = " 테이블 DELETE에 실패하였습니다.";
	static final String FIND_FAIL = " 테이블 FIND에 실패하였습니다.";
	private static final String DEFAULT_MESSAGE = "DB에 문제가 발생하였습니다.";

	SQLAccessException() {
		super(DEFAULT_MESSAGE);
	}

	SQLAccessException(String message) {
		super(message);
	}
}