import {getGridAndPiecesByRoomName} from "../service/chessService.js";
import * as chessBoardFactory from "./chessBoardFactory.js"

export async function createChessBoard(roomName) {
    try {
        const res = await getGridAndPiecesByRoomName(roomName);
        const data = res.data;
        if (data.code !== 200) {
            alert(data.message);
            return;
        }
        chessBoardFactory.createChessBoardAndPieces(data.data.gridDto, data.data.piecesResponseDto);
        hideRoomListPage();
    } catch (e) {
        console.log(e);
    }
}

function hideRoomListPage() {
    const $roomListPage = document.getElementsByClassName("room-list-page");
    $roomListPage[0].classList.remove("hide");
    $roomListPage[0].classList.add("hide");
}