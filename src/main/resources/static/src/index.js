const clickStartButton = () => {
    let roomName = prompt("방 이름");
    if (roomName === undefined) {
        return;
    }
    let password = prompt("비밀번호");
    if (password === undefined ) {
        return;
    }
    console.log(roomName, password);
}