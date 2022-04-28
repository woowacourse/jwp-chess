const createRoom = async () => {
    let title = document.getElementById("title").value;
    let password = document.getElementById("password").value;
    const bodyValue = {
        title: title,
        password: password
    };
    let response = await fetch('/room', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    });

    if (response.ok) {
        let message = await response.text();
        alert(message);
        location.href = "/";
        return;
    }

    let error = await response.json();
    alert(error.message);
}

const getRooms = async () => {
    let rooms = await fetch('/room');
    return rooms;
}

const enterRoom = async (id, title) => {
    let pw = prompt("비밀번호를 입력하세요.");
    const bodyValue = {
        id: id,
        title: title,
        password: pw
    };

    console.log(bodyValue);

    let response = await fetch('/room/' + id, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Accept': 'application/json'
        },
        body: JSON.stringify(bodyValue)
    });

    if (response.ok) {
        let message = await response.text();
        alert(message);
        location.href = "/room/" + id;
        return;
    }

    let error = await response.json();
    alert(error.message);
}