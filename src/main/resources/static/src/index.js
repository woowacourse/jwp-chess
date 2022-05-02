window.addEventListener("load", function () {
    createRoomButtonInitializer.initializeCreateButton();
    readRoomListButtonInitializer.initializeReadRoomListButton();
});

const createRoomButtonInitializer = {
    initializeCreateButton: function () {
        let createButton = document.getElementById('room-create-button');

        createButton.addEventListener("click", () => {
            let roomTitle = document.getElementById("room-title").value;
            let roomPassword = document.getElementById("room-password").value;

            fetch('/rooms', {
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
            if (rooms[i].deleted !== true) {
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
        }
        deleteRoomButtonInitializer.initializeDeleteRoomButton();
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
                            window.location.replace("http://localhost:8080/rooms/" + roomId);
                        }
                    } else {
                        if (httpRequest.readyState === XMLHttpRequest.DONE) {
                            alert('요청을 처리할 수 없습니다');
                            return;
                        }
                    }
                }

                httpRequest.open("POST", "/start/" + roomId);
                httpRequest.send();
            })
        }
    }
}

const deleteRoomButtonInitializer = {
    initializeDeleteRoomButton: function () {
        let deleteButtons = Array.from(document.getElementsByClassName("delete-room"));

        for (let i = 0; i < deleteButtons.length; i++) {
            deleteButtons[i].addEventListener("click", () => {
                let httpRequest = new XMLHttpRequest();

                let roomPassword = document.getElementById("delete-room-password").value;

                httpRequest.onreadystatechange = function () {
                    if (httpRequest.readyState === XMLHttpRequest.DONE && httpRequest.status === 200) {
                        let deletedRoom = JSON.parse(httpRequest.responseText);
                        console.log(deletedRoom);
                        if (deletedRoom.deleted !== true) {
                            alert("비밀번호가 일치하지 않습니다");
                        } else {
                            let deletedSpan = document.getElementById('room-title-' + deletedRoom.roomId);
                            console.log(deletedSpan);
                            deletedSpan.parentElement.innerHTML = "";
                        }
                    } else {
                        if (httpRequest.readyState === XMLHttpRequest.DONE) {
                            alert('요청을 처리할 수 없습니다');
                            return;
                        }
                    }
                }

                let roomId = deleteButtons[i].id.split("-")[1];
                httpRequest.open("delete", "/rooms/" + roomId);
                httpRequest.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
                httpRequest.send(JSON.stringify({ "password": roomPassword }));
            })
        }
    }
}
