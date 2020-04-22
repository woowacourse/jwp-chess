package wooteco.chess;

import java.sql.SQLException;
import java.util.List;

import wooteco.chess.domain.command.Command;
import wooteco.chess.domain.player.User;
import wooteco.chess.dto.LineDto;
import wooteco.chess.dto.RowsDtoConverter;
import wooteco.chess.service.ChessService;
import wooteco.chess.view.InputView;
import wooteco.chess.view.OutputView;

public class Application {

    public static void main(String[] args) throws SQLException {
        ChessService service = new ChessService();
        OutputView.printBoard(service.getEmptyRowsDto());
        User blackUser = createBlackUser();
        User whiteUser = createWhiteUser();
        Command command;

        OutputView.printStart();
        do {
            command = receiveCommand();
            if (command.isStart()) {
                OutputView.printBoard(service.getRowsDto(blackUser, whiteUser));
            }
            if (command.isMove()) {
                executeMovement(service, command, blackUser);
            }
            if (command.isStatus()) {
                OutputView.printScore(service.calculateResult(blackUser));
            }
        } while (command.isNotEnd() && service.checkGameNotFinished(blackUser));
    }

    private static User createBlackUser() {
        try {
            return new User(InputView.receiveBlackUser());
        } catch (IllegalArgumentException e) {
            OutputView.printExceptionMessage(e.getMessage());
            return createBlackUser();
        }
    }

    private static User createWhiteUser() {
        try {
            return new User(InputView.receiveBlackUser());
        } catch (IllegalArgumentException e) {
            OutputView.printExceptionMessage(e.getMessage());
            return createBlackUser();
        }
    }

    private static Command receiveCommand() {
        try {
            return Command.from(InputView.receiveCommand());
        } catch (IllegalArgumentException e) {
            OutputView.printExceptionMessage(e.getMessage());
            return receiveCommand();
        }
    }

    private static void executeMovement(ChessService service, Command command, User blackUser) {
        try {
            List<LineDto> rows = RowsDtoConverter.convertFrom(
                    service.move(blackUser, command.getSource(), command.getTarget()).getBoard());
            OutputView.printBoard(rows);
        } catch (RuntimeException e) {
            OutputView.printExceptionMessage(e.getMessage());
        }
    }
}
