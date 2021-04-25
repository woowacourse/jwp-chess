package chess.webdao;

import chess.webdto.BoardInfosDto;
import chess.webdto.MoveRequestDto;
import chess.webdto.TurnDto;

import java.util.List;

public interface ChessDao {
    int insertMove(String start, String destination);

    List<MoveRequestDto> selectAllMovesByRoomId(int roomId);

    void deleteRoomByRoomId(int roomId);

    void deleteBoardByRoomId(int roomId);

    void changeTurnByRoomId(String turn, boolean isPlaying, int roomId);

    TurnDto selectTurnByRoomId(long roomId);

    long createRoom(String currentTurn, boolean isPlaying);

    void createBoard(String team, String position,String piece, boolean isFirstMoved, long roomId);

    List<BoardInfosDto> selectBoardInfosByRoomId(int i);
}