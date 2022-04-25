async function start() {
    roomName = document.getElementById("roomNameInput").value;
    if (/^\s*$/.test(roomName)) {
        const error = document.getElementById("error");
        error.innerText = "방 이름을 입력하세요!";

        document.getElementById("roomNameInput").value = "";
        return;
    }

    await fetch(`/rooms/${roomName}`, {method: "POST"});
    await fetch(`/rooms/${roomName}/pieces`, {method: "POST"});

    window.location.href = `/rooms/${roomName}`;
}