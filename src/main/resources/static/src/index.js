window.addEventListener("load", function () {
    createRoomButtonInitializer.initializeCreateButton();
    readRoomListButtonInitializer.initializeReadRoomListButton();

    var section = this.document.querySelector(".chess-ui");
    var pngs = section.querySelectorAll(".img");
    var positions = "";

    for (var i = 0; i < pngs.length; i++) {
        pngs[i].onclick = function (event) {
            var parentTarget = event.target.parentElement;
            positions += parentTarget.id;

            if (positions.length == 4) {
                var source = positions.substring(0, 2);
                var target = positions.substring(2, 4);
                JsonSender.sendSourceTarget(source, target);
                positions = "";
            }
        }
    }
});

const JsonSender = {
    sendSourceTarget: function (source, target) {
        fetch('/move', {
            method: "POST",
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
                    console.log(data);
                    if (data.isGameOver === true) {
                        alert(data.winner + "가 승리하였습니다!!!");
                        window.location.replace("end");
                    } else if (data.isMovable === false) {
                        alert('이동할 수 없습니다.');
                    } else {
                        window.location.reload();
                    }
                });
            });
    }
}

const createRoomButtonInitializer = {
    initializeCreateButton: function () {
        let createButton = document.getElementById('room-create-button');

        createButton.addEventListener("click", () => {
            let roomTitle = document.getElementById("room-title").value;
            let roomPassword = document.getElementById("room-password").value;

            fetch('/create', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    roomTitle: roomTitle,
                    roomPassword: roomPassword
                })
            }).then((response) => {
                    return response.json().then((data) => {
                        console.log(data);
                    });
                });
        })
    }
}


const readRoomListButtonInitializer = {
    initializeReadRoomListButton: function () {
        let roomListButton = document.getElementById('room-list');

        roomListButton.addEventListener("click", () => {
            let httpRequest = new XMLHttpRequest();

            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState === XMLHttpRequest.DONE && httpRequest.status === 200) {
                    let rooms = JSON.parse(httpRequest.responseText);
                    console.log(rooms);
                } else {
                    if (httpRequest.readyState === XMLHttpRequest.DONE) {
                        alert('요청을 처리할 수 없습니다');
                        return;
                    }
                }
            }

            httpRequest.open("GET", "/rooms");
            httpRequest.send();
        })
    }
}
