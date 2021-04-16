package chess.web.service;

import chess.console.controller.dto.request.MoveRequestDTO;
import chess.console.controller.dto.response.BoardResponseDTO;
import chess.console.controller.dto.response.BoardStatusResponseDTO;
import chess.console.controller.dto.response.ChessGameResponseDTO;
import chess.console.controller.dto.response.GameStatusResponseDTO;
import chess.console.controller.dto.response.MoveResponseDTO;
import chess.console.controller.dto.response.ResponseDTO;
import chess.web.domain.board.setting.BoardSetting;
import chess.web.domain.game.ChessGame;
import java.sql.SQLException;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("web")
@Service
public class ChessWebService {
    private static final int FILES_SIZE_IN_ONE_RANK = 8;
    private static final int BOARD_ALL_CELLS_SIZE = 64;
    private static final int RANK1_FIRST_INDEX = 0;
    private static final int RANK2_FIRST_INDEX = RANK1_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK3_FIRST_INDEX = RANK2_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK4_FIRST_INDEX = RANK3_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK5_FIRST_INDEX = RANK4_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK6_FIRST_INDEX = RANK5_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK7_FIRST_INDEX = RANK6_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;
    private static final int RANK8_FIRST_INDEX = RANK7_FIRST_INDEX + FILES_SIZE_IN_ONE_RANK;

    private final ChessGame chessGame;
    private final BoardSetting boardSetting;

    public ChessWebService(ChessGame chessGame, BoardSetting boardSetting) {
        this.chessGame = chessGame;
        this.boardSetting = boardSetting;
    }

    public Long createNewChessGame(String title) throws SQLException {
        return chessGame.createNew(boardSetting, title);
    }

    public List<ChessGameResponseDTO> getAllRoomsIdAndTitle() throws SQLException {
        return chessGame.getAllGamesIdAndTitle();
    }

    public MoveResponseDTO requestMove(MoveRequestDTO moveRequestDTO) throws SQLException {
        return createMoveResponse(moveRequestDTO);
    }

    private MoveResponseDTO createMoveResponse(MoveRequestDTO moveRequestDTO) throws SQLException {
        try {
            chessGame.move(moveRequestDTO);
        } catch (Exception e) {
            return new MoveResponseDTO(true, e.getMessage());
        }
        chessGame.changeToNextTurn(moveRequestDTO.getGameId());
        return new MoveResponseDTO(false);
    }

    public ResponseDTO getGameStatus(Long gameId) throws SQLException {
        GameStatusResponseDTO gameStatusResponseDTO = chessGame.getGameStatus(gameId);
        BoardStatusResponseDTO boardStatusResponseDTO = chessGame.getBoardStatus(gameId);
        return new ResponseDTO(
            gameStatusResponseDTO,
            boardStatusResponseDTO.isKingDead(),
            getBoardResponseDTO(boardStatusResponseDTO.getCellsStatus()));
    }

    private BoardResponseDTO getBoardResponseDTO(List<String> cellsStatus) {
        return new BoardResponseDTO(
            cellsStatus.subList(RANK1_FIRST_INDEX, RANK2_FIRST_INDEX),
            cellsStatus.subList(RANK2_FIRST_INDEX, RANK3_FIRST_INDEX),
            cellsStatus.subList(RANK3_FIRST_INDEX, RANK4_FIRST_INDEX),
            cellsStatus.subList(RANK4_FIRST_INDEX, RANK5_FIRST_INDEX),
            cellsStatus.subList(RANK5_FIRST_INDEX, RANK6_FIRST_INDEX),
            cellsStatus.subList(RANK6_FIRST_INDEX, RANK7_FIRST_INDEX),
            cellsStatus.subList(RANK7_FIRST_INDEX, RANK8_FIRST_INDEX),
            cellsStatus.subList(RANK8_FIRST_INDEX, BOARD_ALL_CELLS_SIZE));
    }

    public void deleteGame(Long gameId) throws SQLException {
        chessGame.remove(gameId);
    }
}
