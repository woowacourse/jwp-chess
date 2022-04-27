async function create() {
    let inputName = prompt("방 이름을 입력하세요.");
    let inputPassword = prompt("삭제할 때 사용할 비밀번호를 입력하세요.");

    let response = await fetch("/create", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            name: inputName,
            password: inputPassword
        })
    })

    if (!response.ok) {
        errorMessage = await response.json();
        alert("[ERROR] " + errorMessage.message);
        return;
    }

    alert("방이 생성되었습니다!");
}

async function showList() {
    let rooms;
    await fetch("/list")
        .then(res => res.json())
        .then(data => rooms = data)

    console.log(rooms)
    const roomList = document.getElementById("room-list")
    for (const aRoom of rooms) {
        let aTag = document.createElement("a");
        let roomItem = document.createElement("li")
        let delBtn = document.createElement("button")
        delBtn.id = aRoom.id
        delBtn.innerText = "삭제"
        delBtn.classList.add("chess-del-btn")
        roomItem.classList.add("room-items")
        aTag.href = "/rooms/" + aRoom.id
        aTag.innerText = aRoom.name
        roomItem.appendChild(aTag)
        roomItem.appendChild(delBtn)
        roomList.appendChild(roomItem)
    }
}
