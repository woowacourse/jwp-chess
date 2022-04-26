package chess.config;

import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.gameflow.AlternatingGameFlow;
import chess.gameflow.GameFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DiConfig {

    @Bean
    @Scope("session")
    public ChessBoard chessBoard() {
        BoardFactory boardFactory = RegularBoardFactory.getInstance();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory.create(), gameFlow);
    }
}
