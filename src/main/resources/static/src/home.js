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

const removeUrl = `/room/delete`;

const deleteRoom = (event) => {
    const id = event.target.dataset.id;
    console.log(removeUrl);
    const password = prompt(`${id} 번방 비밀번호를 입력해주세요.`);

    fetch(removeUrl, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            id: id,
            password: password
        })
    }).then(res => res.json())
        .then(json => {
            alert(json.message);
            location.href = '/';
        });
}
