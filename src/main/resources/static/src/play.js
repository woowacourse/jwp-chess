window.addEventListener("DOMContentLoaded", function () {
    boardDrawer.loadBoard();
});

const moveInitializer = {
    initializeMove: function() {
        let pngs = Array.from(document.getElementsByClassName("img"));
        let positions = "";

        for (let i = 0; i < pngs.length; i++) {
            pngs[i].onclick = function (event) {
                let parentTarget = event.target.parentElement;
                positions += parentTarget.id;
                if (positions.length == 4) {
                    let source = positions.substring(0, 2);
                    let target = positions.substring(2, 4);
                    let roomId = window.location.pathname.split("/")[2];
                    JsonSender.sendSourceTarget(source, target, roomId);
                    positions = "";
                }
            }
        }
    }
}

const JsonSender = {
    sendSourceTarget: function (source, target, roomId) {
        fetch('/rooms/' + roomId, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                source: source,
                target: target
            })
        })
            .then((response) => {
                return response.json().then((data) => {
                    if (data.isGameOver === true) {
                        alert(data.winner + "가 승리하였습니다!!!");
                        window.location.replace("http://localhost:8080/");
                    } else if (data.isMovable === false) {
                        alert('이동할 수 없습니다.');
                    } else {
                        boardDrawer.loadBoard();
                    }
                });
            });
    }
}

const boardDrawer = {
    loadBoard: function() {
        let httpRequest = new XMLHttpRequest();

        httpRequest.onreadystatechange = function() {
            if (httpRequest.readyState === XMLHttpRequest.DONE && httpRequest.status === 200) {
                let chessBoard = JSON.parse(httpRequest.responseText);
                console.log(chessBoard);
                boardDrawer.draw(chessBoard.board);
            } else {
                if (httpRequest.readyState === XMLHttpRequest.DONE) {
                    alert('요청을 처리할 수 없습니다');
                    return;
                }
            }
        }

        let roomId = window.location.pathname.split("/")[2];
        httpRequest.open("POST", "/rooms/" + roomId + "/play");
        httpRequest.send();
    },

    initBoard: function() {
        let rows = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];
        let columns = ['1', '2', '3', '4', '5', '6', '7', '8'];
        for (let i = 0; i < rows.length; i++) {
            for (let j = 0; j < columns.length; j++) {
                let parent = document.getElementById(rows[i]+columns[j]);
                let child = parent.firstChild;
                if (child !== null) {
                    parent.removeChild(child);
                }
            }
        }
    },

    draw: function(chessBoard) {
        boardDrawer.initBoard();
        let board = chessBoard;

        let rows = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
        let columns = ['1', '2', '3', '4', '5', '6', '7', '8'];
        for (let i = 0; i < rows.length; i++) {
            for (let j = 0; j < columns.length; j++) {
                let id = rows[i]+columns[j];
                id = id.toLowerCase();
                let parent = document.getElementById(id);
                let img = document.createElement("img");

                if (board[rows[i]+columns[j]]) {
                    let color =  board[rows[i]+columns[j]].color;
                    let name =  board[rows[i]+columns[j]].name;
                    img.src = `/images/${name}_${color}.png`;
                    img.classList.add("img", "piece-image");
                    parent.insertBefore(img, parent.firstChild);
                } else {
                    let emptyImg = document.createElement("img");
                    emptyImg.className = "img";
                    emptyImg.src = "/images/empty.png"
                    parent.appendChild(emptyImg);
                }
            }
        }
        moveInitializer.initializeMove();
    }
}

