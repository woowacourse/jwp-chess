window.addEventListener("load", function () {
    createRoomButtonInitializer.initializeCreateButton();
    readRoomListButtonInitializer.initializeReadRoomListButton();
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
                    roomListDrawer.showRooms(rooms);
                    roomListDrawer.initTitlesAndButtons();
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

const roomListDrawer = {
    showRooms: function(rooms) {
        this.removeAll();
        let parent = document.getElementById("room-list-container");
        let tmp = document.createElement('div');
        rooms = rooms.rooms;
        for (let i = 0; i < rooms.length; i++) {
            let roomId = rooms[i].roomId;
            let roomTitle = rooms[i].roomTitle;

            let roomIdElement = document.createTextNode(roomId);
            let space = document.createTextNode( '\u00A0\u00A0' )
            let roomTitleElement = document.createElement("span");
            roomTitleElement.innerText = roomTitle;
            roomTitleElement.id = "room-title-" + roomId;
            roomTitleElement.className = "room-title-play";

            let br = document.createElement('br');
            let button = document.createElement("button");
            button.type = "submit";
            button.innerHTML = "삭제";
            button.id = "delete-" + roomId;
            button.className = "delete-room";

            tmp.append(roomIdElement);
            tmp.append(space);
            tmp.append(roomTitleElement);
            tmp.append(button);
            tmp.append(br);
            parent.append(tmp);
        }
    },

    removeAll: function() {
        let parent = document.getElementById("room-list-container");
        let child = parent.firstChild;
        while (child != null) {
            parent.removeChild(child);
            child = parent.firstChild;
        }
    },

    initTitlesAndButtons: function () {
        let playRooms = Array.from(document.getElementsByClassName("room-title-play"));
        for (let i = 0; i < playRooms.length; i++) {
            playRooms[i].addEventListener("click", () => {
                let roomId = playRooms[i].id.split("-")[2];
                let httpRequest = new XMLHttpRequest();

                httpRequest.onreadystatechange = function() {
                    if (httpRequest.readyState === XMLHttpRequest.DONE && httpRequest.status === 200) {
                        let room = JSON.parse(httpRequest.responseText);
                        if (room.finished === true) {
                            alert("이미 종료된 게임입니다");
                        } else {
                            window.location.replace("http://localhost:8080/play/" + roomId);
                        }
                    } else {
                        if (httpRequest.readyState === XMLHttpRequest.DONE) {
                            alert('요청을 처리할 수 없습니다');
                            return;
                        }
                    }
                }

                httpRequest.open("GET", "/start/" + roomId);
                httpRequest.send();
            })
        }
        console.log(playRooms);
    }
}


