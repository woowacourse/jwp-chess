window.onload = function () {
    const pieces = document.getElementsByClassName('piece');
    Array.from(pieces).forEach((el) => {
        el.addEventListener('click', click());
    })

    const deleteBtn = document.getElementsByClassName("delete-btn");
    Array.from(deleteBtn).forEach((el) => {
        el.addEventListener('click', deleteRoom());
    })
}

let state = "stay"; // stay, show
let source = "";
let target = "";

function enterNewGame() {
    const form = document.createElement("form");
    form.setAttribute("charset", "UTF-8");
    form.setAttribute("method", "Post");
    form.setAttribute("action", "/room/create");

    const roomName = document.getElementById("game-name-input").value;
    const roomNameField = document.createElement("input");
    roomNameField.setAttribute("type", "hidden");
    roomNameField.setAttribute("name", "roomName");
    roomNameField.setAttribute("value", roomName);
    form.appendChild(roomNameField);

    document.body.appendChild(form);
    form.submit();
}

function deleteRoom() {
    return function (event) {
        const form = document.createElement("form");
        form.setAttribute("charset", "UTF-8");
        form.setAttribute("method", "Post");
        form.setAttribute("action", "/room/delete");

        const roomId = event.target.id;
        const roomIdField = document.createElement("input");
        roomIdField.setAttribute("type", "hidden");
        roomIdField.setAttribute("name", "roomId");
        roomIdField.setAttribute("value", roomId);
        form.appendChild(roomIdField);

        document.body.appendChild(form);
        form.submit();
    }
}

function click() {
    return function (event) {
        if (state === "stay") {
            show(event.target);
            state = "show";
            source = event.target.id;
            return;
        }

        if (state === "show") {
            clickWhereToMove(event.target);
            state = "stay";
            source = "";
            target = "";
            return;
        }
    }
}

function clickWhereToMove(eventTarget) {
    if (checkIsValidTarget(eventTarget)) {
        target = eventTarget.id;
        submitMove(source, target);
    }

    const piece = document.getElementsByClassName("piece");
    for (let i = 0; i < piece.length; i++) {
        piece[i].classList.remove("moveAble");
        piece[i].style.backgroundColor = "";
    }
}

function submitMove(src, tar) {
    const roomId = document.getElementById("roomId").innerText;

    const form = document.createElement("form");
    form.setAttribute("charset", "UTF-8");
    form.setAttribute("method", "Post");
    form.setAttribute("action", "/game/move/" + roomId);

    const sourceField = document.createElement("input");
    sourceField.setAttribute("type", "hidden");
    sourceField.setAttribute("name", "source");
    sourceField.setAttribute("value", src);
    form.appendChild(sourceField);

    const targetField = document.createElement("input");
    targetField.setAttribute("type", "hidden");
    targetField.setAttribute("name", "target");
    targetField.setAttribute("value", tar);
    form.appendChild(targetField);

    document.body.appendChild(form);
    form.submit();
}

function checkIsValidTarget(target) {
    return target.classList.contains("moveAble");
}

function show(target) {
    const roomId = document.getElementById("roomId").innerText;
    const requestQuery = "source=" + target.id;

    $.ajax({
        url: "/game/show/" + roomId,
        type: "GET",
        data: requestQuery,
        success: function (result) {
            //[d2, d3]
            if (result !== null && result !== "[]") {
                const positions = result.slice(1, -1).split(", ");
                positions.forEach((el) => {
                    const piece = document.getElementById(el);
                    piece.classList.add("moveAble");
                    piece.style.backgroundColor = "skyblue";
                });
            }
        },
        error: function (result) {
            alert("에러 발생");
        }
    })
}