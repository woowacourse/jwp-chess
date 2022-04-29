function openPop() {
    document.getElementById("popup_layer").style.display = "block";
}

//팝업 닫기
function closePop() {
    document.getElementById("popup_layer").style.display = "none";
}

function openDeletePop(id) {
    console.log(id);
    const data = prompt("비밀번호를 입력하세요.");

    const bodyValue = {
        pw: data,
        id: id,
    };

    fetch(`/rooms/${id}`, {
        method: "GET",
    })
        .then(res => res.json())
        .then(res => {
            if (res.gameStatus === "PLAYING") {
                alert("게임이 실행중이라 삭제할 수 없습니다.");
            } else {
                fetch("/room", {
                    method: 'DELETE',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(bodyValue)
                }).then(async res => {
                    if (!res.ok) {
                        const data = await res.json();
                        throw new Error(data.message);
                    }
                })
                    .catch(reason => alert(reason))
                    .finally(() => location.reload());
             //   window.location.reload();
            }
        })
}

//팝업 닫기
function closeDeletePop() {
    document.getElementById("delete").style.display = "none";
}


window.onload = createRoomList;

async function createRoomList() {
    await fetch("/list")
        .then(res => res.json())
        .then(res => {
            getList(res);
        })
}

function getList(res) {
    res.forEach((data) => {
        let eachDiv = document.getElementById("start");
        let tr = document.createElement("tr");
        eachDiv.appendChild(tr);
        let td = document.createElement("td");
        let div = document.createElement("div");
        div.setAttribute("id", data.id)
        div.setAttribute("onclick", "enterRoom(id)");
        div.innerText = data.name;
        td.appendChild(div);
        tr.appendChild(td);

        let td2 = document.createElement("td");
        let div2 = document.createElement("div");

        div2.setAttribute("id", data.id);
        div2.setAttribute("onclick", "openDeletePop(id)");
        div2.innerText = "삭제";
        td2.appendChild(div2);
        tr.appendChild(td2);
    })
}

async function enterRoom(id) {
    // console.log();
    window.location.replace("/room?id=" + id);
}

