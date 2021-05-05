window.onload = function () {
    const pieces = document.getElementsByClassName('piece');
    Array.from(pieces).forEach((el) => {
        el.addEventListener('click', click());
    })

    const enterBtn = document.getElementsByClassName("enter-btn");
    Array.from(enterBtn).forEach((el) => {
        el.addEventListener('click', enterRoom());
    })

    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteRoom());
    })
}

let state = "stay"; // stay, show
let source = "";
let target = "";

function enterNewGame() {
    const roomName = document.getElementById("game-name-input").value;
    const player1 = prompt("플레이어1의 비밀번호를 입력하시오");
    const jsonData = JSON.stringify({roomName: roomName, player1: player1});

    $.ajax({
        url: "/room",
        type: "POST",
        data: jsonData,
        contentType: 'application/json',
        success: function (roomId) {
            alert(roomName + " 방 생성");
            location.href = "/game/load/" + roomId;
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function enterRoom() {
    return function (event) {
        const roomId = event.target.id;
        const player2 = prompt("플레이어의 비밀번호를 입력하시오");

        $.ajax({
            url: "/room/enter/" + roomId,
            type: "POST",
            data: player2,
            contentType: 'application/json',
            success: function () {
                alert("방 " + roomId + "에 입장합니다.");
                location.href = "/game/load/" + roomId;
            },
            error: function (e) {
                alert(e.responseText);
            }
        })
    }
}

function deleteRoom() {
    return function (event) {
        const roomId = event.target.id;
        $.ajax({
            url: "/room/" + roomId,
            type: "DELETE",
            success: function () {
                alert("삭제 완료");
                location.href = "/room/list";
            },
            error: function (e) {
                alert(e.responseText);
            }
        })
    }
}

function click() {
    return function (event) {
        if (state === "stay") {
            show(event.target);
            state = "show";
            source = event.target.id;
            return;
        }

        if (state === "show") {
            clickWhereToMove(event.target);
            state = "stay";
            source = "";
            target = "";
            return;
        }
    }
}

function show(target) {
    const roomId = document.getElementById("roomId").innerText;
    const requestQuery = "source=" + target.id;

    $.ajax({
        url: "/game/reachable/" + roomId,
        type: "GET",
        data: requestQuery,
        contentType: "application/json",
        success: function (result) {
            let positions = result.positions;
            //[d2, d3]
            if (result !== null) {
                positions.forEach((el) => {
                    const piece = document.getElementById(el);
                    piece.classList.add("moveAble");
                    piece.style.backgroundColor = "skyblue";
                });
            }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function clickWhereToMove(eventTarget) {
    if (checkIsValidTarget(eventTarget)) {
        target = eventTarget.id;
        submitMove(source, target);
    }

    const piece = document.getElementsByClassName("piece");
    for (let i = 0; i < piece.length; i++) {
        piece[i].classList.remove("moveAble");
        piece[i].style.backgroundColor = "";
    }
}

function submitMove(src, tar) {
    const roomId = document.getElementById("roomId").innerText;
    const jsonData = JSON.stringify({source: src, target: tar});

    $.ajax({
        url: "/game/move/" + roomId,
        type: "POST",
        data: jsonData,
        contentType: "application/json",
        success: function (result) {
            location.href = result;
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function checkIsValidTarget(target) {
    return target.classList.contains("moveAble");
}