function makeRoom() {

    let roomName = document.getElementById("title").value;
    let whiteName = document.getElementById("firstMemberName").value;
    let blackName = document.getElementById("secondMemberName").value;

    if (roomName === '' || whiteName === '' || blackName === '') {
        alert("입력값을 모두 입력해주세요.");
        return;
    }

    let roomForm = document.getElementById("roomForm");
    roomForm.submit();
}
