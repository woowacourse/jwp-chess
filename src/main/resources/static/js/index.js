let indexPage;

document.addEventListener("DOMContentLoaded", function () {
    indexPage = new IndexPage();
    indexPage.initIndexPage();
});

document.querySelector(".make-room-button").addEventListener("click", function () {
    let newRoomTitle = document.querySelector(".newRoomId").value;
    fetch(indexPage.postRoomUrl + '?title=' + newRoomTitle, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => res.json())
        .then(data => location.href = 'chess/' + data);
});

function IndexPage() {
    this.getRoomsUrl = window.location.origin + "/api/rooms";
    this.postRoomUrl = window.location.origin + "/api/room";
}

IndexPage.prototype.initIndexPage = function () {
    const roomList = document.querySelector(".room-list");

    fetch(indexPage.getRoomsUrl, {
        method: 'GET'
    }).then(res => res.json())
        .then(async function (data) {
            for (let i = 0; i < data.roomNames.length; i++) {
                let sd = await postRoomId(data.roomNames[i]);
                roomList.innerHTML +=
                    `<li class="room">
                        <button class="room-button" onclick="location.href = 'chess/' + ${sd}">
                        ${data.roomNames[i]}
                        </button>
                    </li>`;
            }
        });
}

async function postRoomId(roomTitle) {
    return await fetch(indexPage.postRoomUrl + '?title=' + roomTitle, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => res.json());
}