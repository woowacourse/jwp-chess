
// -------- init start ---------
function setUpIndex() {

    const createRoomForm = document.getElementById("create_room_form");
    createRoomForm.addEventListener("submit", e => {
        e.preventDefault();
        let roomName = new FormData(createRoomForm).get("room_name");
        let roomPassword = new FormData(createRoomForm).get("room_password");
        send("/rooms", {
                method: 'post',
                body: JSON.stringify({'name': roomName, 'password': roomPassword}),
                headers: new Headers({'Content-Type': 'application/json'})
            }, relocate);
    })

    console.log("setupIndex done")
}
// -------- init end ---------

// ------------- utils start -----------------
function relocate(responseJson) {
    console.log("responseJson in relocate =", responseJson);
    window.location.href = responseJson['url'];
}


function toJSON(form) {
    const json = {};
    const formData = new FormData(form);
    Array.from(formData.entries()).forEach(([key, value]) => {
        json[key] = value;
    })
    return JSON.stringify(json);
}

function remove(roomId) {
    var roomPassword = prompt("비밀번호를 입력해주세요.");
    send("/rooms/" + roomId, {
        method: 'delete',
        body: JSON.stringify({'id': roomId, 'password': roomPassword}),
        headers: new Headers({'Content-Type': 'application/json'})
    }, relocate);
}

function enter(roomId) {
    send("/rooms/" + roomId + "/enter", {
        method: 'get'
    }, relocate);
}

async function send(path, fetchBody, handler) {
    async function alertIfException(responseJson) {
        console.log("toJSON = ", responseJson);
        if (responseJson['exception']) {
            alert(responseJson['exception']);
            return null;
        }
        return responseJson;
    }

    let response = await fetch(path, fetchBody);
    let responseJson = await response.json();
    if (await alertIfException(responseJson)) {
        handler(responseJson);
    }
}
// --------------- utils end ------------------
