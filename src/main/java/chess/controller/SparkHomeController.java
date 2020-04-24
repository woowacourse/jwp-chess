package chess.controller;

import spark.Spark;

import java.util.Collections;

public class SparkHomeController {
	private static final String STATIC_PATH = "/index.html";
	public static final String PATH = "/chess/home";

	private static final SparkHomeController CHESS_HOME_CONTROLLER;

	static {
		CHESS_HOME_CONTROLLER = new SparkHomeController();
	}

	private SparkHomeController() {
	}

	public static SparkHomeController getInstance() {
		return CHESS_HOME_CONTROLLER;
	}

	public void run() {
		routeGetMethod();
	}

	private void routeGetMethod() {
		Spark.get(PATH, (request, response)
				-> Renderer.getInstance().render(Collections.emptyMap(), STATIC_PATH));
	}
}
