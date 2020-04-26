package chess.service;

import chess.dao.AnnouncementDao;
import chess.dao.PieceDao;
import chess.dao.StateDao;
import chess.dao.StatusRecordDao;
import chess.domain.coordinate.Coordinate;
import chess.domain.pieces.Piece;
import chess.domain.pieces.PieceType;
import chess.domain.pieces.Pieces;
import chess.domain.pieces.StartPieces;
import chess.domain.state.State;
import chess.domain.state.StateType;
import chess.domain.team.Team;
import chess.dto.PieceDto;
import chess.dto.StateDto;
import chess.view.Announcement;
import chess.view.BoardToHtml;
import chess.view.board.Board;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChessRoomService {
	private final StateDao stateDao;
	private final PieceDao pieceDao;
	private final StatusRecordDao statusRecordDao;
	private final AnnouncementDao announcementDao;

	public ChessRoomService(final StateDao stateDao, final PieceDao pieceDao
			, final StatusRecordDao statusRecordDao, final AnnouncementDao announcementDao) {
		this.stateDao = stateDao;
		this.pieceDao = pieceDao;
		this.statusRecordDao = statusRecordDao;
		this.announcementDao = announcementDao;
	}

	public String loadBoardHtml(final int roomId) throws SQLException {
		final State state = loadState(roomId);

		return createBoardHtml(state);
	}

	private Set<Piece> createPieceSet(final List<PieceDto> pieceDtos) {
		return pieceDtos.stream()
				.map(daoPieceDto -> PieceType.getFactoryOfName(daoPieceDto.getPieceType()).apply(
						Team.of(daoPieceDto.getTeam()), Coordinate.of(daoPieceDto.getCoordinate())))
				.collect(Collectors.toSet());
	}

	private State loadState(final int roomId) throws SQLException {
		final StateDto stateDto = stateDao.findStateByRoomId(roomId);
		final List<PieceDto> pieceDtos = pieceDao.findPiecesByRoomId(roomId);

		final Set<Piece> pieces = createPieceSet(pieceDtos);
		return StateType.getFactory(stateDto.getState()).apply(
				new Pieces(pieces));
	}

	private String createBoardHtml(final State state) {
		final List<List<String>> board = Board.of(state.getSet()).getLists();
		return BoardToHtml.of(board).getHtml();
	}

	public int saveNewState(final int roomId) throws SQLException {
		return stateDao.addState("ended", roomId);
	}

	public void saveNewPieces(final int roomId) throws SQLException {
		final Set<Piece> pieces = new StartPieces().getInstance();
		for (Piece piece : pieces) {
			pieceDao.addPiece(piece.getPieceTypeName(), piece.getTeamName(),
					piece.getCoordinateRepresentation(), roomId);
		}
	}

	public void updateRoom(final int roomId, final String command) throws SQLException {
		final State before = loadState(roomId);
		final State after = before.pushCommand(command);
		stateDao.setStateByRoomId(roomId, after.getStateName());
		pieceDao.deletePieces(roomId);
		savePiecesFromState(roomId, after);
		saveStatusRecordIfEnded(roomId, after);
		announcementDao.setAnnouncementByRoomId(roomId, createRightAnnouncement(after));
	}

	private void savePiecesFromState(final int roomId, final State after) throws SQLException {
		for (final Piece piece : after.getSet()) {
			pieceDao.addPiece(piece.getPieceTypeName(), piece.getTeamName(),
					piece.getCoordinateRepresentation(), roomId);
		}
	}

	private void saveStatusRecordIfEnded(final int roomId, final State after) throws SQLException {
		if (after.isEnded()) {
			statusRecordDao.addStatusRecord(Announcement.ofStatus(after.getPieces()).getString(), roomId);
		}
	}

	private static String createRightAnnouncement(State state) {
		if (state.isPlaying()) {
			return Announcement.ofPlaying().getString();
		}
		if (state.isReported()) {
			return Announcement.ofStatus(state.getPieces()).getString();
		}
		return Announcement.ofEnd().getString();
	}

	public String loadAnnouncementMessage(int roomId) throws SQLException {
		return announcementDao.findAnnouncementByRoomId(roomId).getMessage();
	}

	public int saveNewAnnouncementMessage(int roomId) throws SQLException {
		return announcementDao.addAnnouncement(Announcement.ofFirst().getString(), roomId);
	}

	public int saveAnnouncementMessage(final int roomId, final String message) throws SQLException {
		return announcementDao.setAnnouncementByRoomId(roomId, message);
	}
}
