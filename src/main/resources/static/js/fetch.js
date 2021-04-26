export async function getNewBoard() {
    const response = await fetch(url+ "/start");
    return await response.json();
}

export async function getScores() {
    const response = await fetch(url + "/score");
    return await response.json();
}

export async function move(from, to) {
    const data = await fetch(url +"/move", {
        method: "POST",
        body: JSON.stringify({from, to}),
        headers: {'Content-Type': 'application/json'}
    });
    return await data.json();
}

export async function getTurn() {
    const response = await fetch(url +"/turn");
    return await response.json();
}

export async function getStatus() {
    const response = await fetch(url +"/status");
    return await response.json();
}

export async function getPath(from) {
    const data = await fetch(url + "/path", {
        method: "POST",
        body: JSON.stringify({from}),
        headers: {"Content-Type": "application/json"}
    });
    return await data.json();
}

export async function loadBoard() {
    const response = await fetch(url + "/load");
    return await response.json();
}

const url = "http://localhost:8080/rooms/" + getRoomId() + "/board";

function getRoomId(){
    const path = window.location.pathname;
    console.log(path);
    const roomId = path.split('/')[2];
    console.log(roomId);
    return roomId;
}

// Room fetch
export async function makeRoom(name) {
    const response = await fetch("http://localhost:8080/rooms", {
        method: "POST",
        body: JSON.stringify({name}),
        headers: {"Content-Type": "application/json; charset=UTF-8"}
    });
    location.href = response.url;
}

export async function deleteRoom(roomId) {
    const response = await fetch("http://localhost:8080/rooms/" + roomId, {
        method: "POST",
        headers: {"Content-Type": "application/json; charset=UTF-8"}
    })
        .then((response) => {
            alert('방을 삭제했습니다.');
            location.href = response.url
        });
}

export async function loadGame(roomId) {
    location.href = "/rooms/" + roomId +  "/board";
    const response = await fetch("http://localhost:8080/rooms/" + roomId + "/board/load");
    return response.json();
}
