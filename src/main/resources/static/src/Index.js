let first = '';
let second = '';

function clicked(id) {
    setVariable(id);
    changeClickedBackground(id);
    checkSendToServer();
}

function setVariable(id) {
    if (first !== '') {
        second = id;
        return;
    }
    first = id;
}

function changeClickedBackground(id) {
    let elementById = document.getElementById(id);
    elementById.style.backgroundColor = "#ff0";
}

function checkSendToServer() {
    if (first !== '' && second !== '') {
        sendToServer(first, second);
        first = '';
        second = '';
    }
}

function checkStatus() {
    let element = document.getElementById("roomId");

    fetch(`/room/${element.value}/status`, {
        method: "GET",
        headers: {
            "Content-Type": "text/plain",
        }
    }).then((response) => {
        response.json().then(data => {
            document.getElementById("statusResult")
                .innerHTML = "검은 말 : " + data.score.BLACK + "<br>"
                + "흰말 : " + data.score.WHITE + "<br>"
                + data.winner;
        });
    });
}

function sendToServer(first, second) {
    let element = document.getElementById("roomId");
    const moveCommand = {"source": first, "target": second};
    let moveUrl = '/room/' + element.value + '/move';
    fetch(moveUrl, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(moveCommand)
    }).then((response) => {
        response.json()
            .then(data => {
                console.log(data);
                if (response.status === 400) {
                    alert(data.message);
                }
                if (data.finished === true) {
                    alert("게임이 종료되었습니다.");
                    document.location.href = `/`
                    return;
                }
                location.reload();
            })
    })
        .catch((error) => {
            console.log(error);
        });
}

