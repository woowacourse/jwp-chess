import {chessBoard, gameResultWindow, initChessBoard} from "./initialize.js";
import {addChessBoardEvent, checkIsPlaying, player1, player2} from "./movement.js";
import ChessService from "./ChessService.js";

const apiService = new ChessService();

loadFirstPage();

function loadFirstPage() {
    chessBoard.style.display = "none";
    gameResultWindow.style.display = "none";
    player1.style.display = "none";
    player2.style.display = "none";

    loadPrevGame()
}

function getRoomId() {
    const path = window.location.pathname;
    const roomId = path.split('/')[2];
    return roomId;
}

function loadPrevGame() {
    const password = prompt('비밀번호를 입력하세요~ :)');
    apiService.checkAllowedUser(getRoomId(), {password})
        .then(data => {
            if (!data['allowed']) {
                location.href = "/";
            }
        })
    apiService.loadPrevGame(getRoomId())
        .then(data => {
            initializeChessBoard(data);
            checkIsPlaying(data);
        })
        .catch(error => {
            console.log(error)
        })
}

function initializeChessBoard(data) {
    initChessBoard(data);
    addChessBoardEvent();
    chessBoard.style.display = "flex";
    player1.style.display = "flex";
    player2.style.display = "flex";
}
