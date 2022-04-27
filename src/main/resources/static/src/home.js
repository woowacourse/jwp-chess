function makeRoom() {

    let roomName = document.getElementById("roomName").value;
    let password = document.getElementById("password").value;
    let whiteName = document.getElementById("whiteName").value;
    let blackName = document.getElementById("blackName").value;

    if (roomName === '' || password === '' || whiteName === '' || blackName === '') {
        alert("입력값을 모두 입력해주세요.");
        return;
    }

    let roomForm = document.getElementById("roomForm");
    roomForm.submit();
}
