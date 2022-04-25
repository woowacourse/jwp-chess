package chess.config;

import chess.domain.board.ChessBoard;
import chess.domain.board.factory.BoardFactory;
import chess.domain.board.factory.RegularBoardFactory;
import chess.turndecider.AlternatingGameFlow;
import chess.turndecider.GameFlow;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ChessGameConfig {

    @Bean
    public BoardFactory boardFactory() {
        return RegularBoardFactory.getInstance();
    }

    @Bean
    @Scope("prototype")
    public ChessBoard chessBoard() {
        BoardFactory boardFactory = boardFactory();
        GameFlow gameFlow = new AlternatingGameFlow();
        return new ChessBoard(boardFactory.create(), gameFlow);
    }
}
