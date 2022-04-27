
// -------- init start ---------
function setUpIndex() {

    const createRoomForm = document.getElementById("create_room_form");
    createRoomForm.addEventListener("submit", e => {
        e.preventDefault();
        let roomName = new FormData(createRoomForm).get("room_name");
        let roomPassword = new FormData(createRoomForm).get("room_password");
        send("/rooms/create", {
                method: 'post',
                body: JSON.stringify({'roomName': roomName, 'roomPassword': roomPassword}),
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

function remove(roomName) {
    var roomPassword = prompt("비밀번호를 입력해주세요.");
    send("/rooms/remove", {
        method: 'post',
        body: JSON.stringify({'roomName': roomName, 'roomPassword': roomPassword}),
        headers: new Headers({'Content-Type': 'application/json'})
    }, relocate);
}

function enter(roomName) {
    send("/rooms/enter/" + roomName, {
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
