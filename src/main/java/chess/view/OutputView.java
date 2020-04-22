package chess.view;

import chess.model.domain.board.Square;
import chess.model.domain.piece.Team;
import chess.model.dto.ChessGameDto;
import java.util.List;
import java.util.Map;

public class OutputView {

    private OutputView() {
    }

    public static void printStartInfo() {
        System.out.println("> 체스 게임을 시작합니다.");
        System.out.println("> 게임 시작 : start");
        System.out.println("> 게임 종료 : end");
        System.out.println("> 게임 이동 : move source위치 target위치 - 예. move b2 b3");
        System.out.println("> 현재 점수 및 고득점자 확인 : status");
    }

    public static void printChessBoard(ChessGameDto chessGameDto) {
        List<String> pieces = chessGameDto.getPieces();
        int maxSize = Square.MAX_FILE_AND_RANK_COUNT;
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < maxSize; j++) {
                System.out.print(getPieceLetter(pieces.get(i * maxSize + j)));
            }
            System.out.println();
        }
    }

    private static String getPieceLetter(String name) {
        if (name.isEmpty()) {
            return ".";
        }
        return name;
    }

    public static void printCanNotMove() {
        System.out.println("해당 좌표로는 이동할 수 없습니다.");
    }

    public static void printWinner(Team team) {
        System.out.println(team.getName() + "(이)가 승리했습니다");
    }

    public static void printScore(Map<Team, Double> teamScore) {
        for (Team team : teamScore.keySet()) {
            System.out.println(team.getName() + "의 점수는 " + teamScore.get(team) + "입니다.");
        }
    }

    public static void printWinners(List<Team> winners) {
        System.out.print("고득점자는 ");
        for (Team team : winners) {
            System.out.print(team.getName() + " ");
        }
        System.out.println("입니다.");
    }

    public static void printCanNotStart() {
        System.out.println("현재 게임 진행중이므로 START 할 수 없습니다.");
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void printNotMyTurn(Team team) {
        System.out.println("지금은 " + team.name() + "의 차례입니다.");
    }

    public static void printNoPiece() {
        System.out.println("움직일 수 있는 피스가 없습니다.");
    }
}

