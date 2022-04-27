function createRoom() {
    let title = document.getElementById('room-title').value;
    let password = document.getElementById('room-password').value;

    const request = {
        title: title,
        password: password
    };

    fetch('/chess', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(request),
    }).then(res => res.json())
        .then(value => alert("생성된 방 번호는 " + value + "입니다."));
}

