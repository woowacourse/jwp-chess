package chess.console.view;

import chess.web.controller.dto.response.GameStatusResponseDto;

public class OutputView {

    private static final int BOARD_ALL_CELLS_SIZE = 64;
    private static final int BOARD_WIDTH_SIZE = 8;

    private OutputView() {
    }

    public static void printGameStartMessage() {
        System.out.println("체스 게임을 시작합니다.");
        System.out.println("게임 시작은 start, 종료는 end 명령을 입력하세요.");
    }

    public static void printGameStatus(GameStatusResponseDto gameStatusResponseDto) {
        System.out.println();
        for (int cellIndex = 0; cellIndex < BOARD_ALL_CELLS_SIZE; cellIndex++) {
            printCellStatus(gameStatusResponseDto.getBoardStatus(), cellIndex);
        }
        if (!gameStatusResponseDto.isKingDead()) {
            System.out.println("현재 " + gameStatusResponseDto.getCurrentTurnTeamColorName() + " 팀의 차례입니다.");
            return;
        }
        System.out.println(gameStatusResponseDto.getBeforeTurnTeamColorName() + " 팀이 이겼습니다.");
    }

    private static void printCellStatus(String boardStatus, int cellIndex) {
        System.out.print(boardStatus.charAt(cellIndex));
        int cellOrder = cellIndex + 1;
        if (cellOrder % BOARD_WIDTH_SIZE == 0) {
            System.out.println();
        }
    }

    public static void printScores(GameStatusResponseDto gameStatusResponseDto) {
        double blackTeamScore = gameStatusResponseDto.getBlackPlayerScore();
        double whiteTeamScore = gameStatusResponseDto.getWhitePlayerScore();
        System.out.printf("흑 팀 점수 : %.1f, 백 팀 점수 : %.1f\n", blackTeamScore, whiteTeamScore);
    }
}
