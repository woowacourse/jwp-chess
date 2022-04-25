async function start() {
    roomName = document.getElementById("roomNameInput").value;
    password = document.getElementById("passwordInput").value;
    if (/^\s*$/.test(roomName)) {
        const error = document.getElementById("error");
        error.innerText = "방 이름을 입력하세요!";

        document.getElementById("roomNameInput").value = "";
        return;
    }

    if (/^\s*$/.test(password)) {
        const error = document.getElementById("error");
        error.innerText = "비밀번호를 입력하세요!";

        document.getElementById("password").value = "";
        return;
    }

    const res = await fetch("/rooms", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({roomName: `${roomName}`, password: `${password}`})
    });
    if (res.status === 201) {
        await fetch(`/rooms/${roomName}/pieces`, {method: "POST"});
        window.location.href = res.headers.get("location");
    }
}