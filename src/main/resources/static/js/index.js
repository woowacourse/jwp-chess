let indexPage;

document.addEventListener("DOMContentLoaded", function () {
    indexPage = new IndexPage();
    indexPage.initIndexPage();
});

document.querySelector(".make-room-button").addEventListener("click", function () {
    let newRoomId = document.querySelector(".newRoomId").value;
    location.href = 'chess/' + newRoomId;
});

function IndexPage() {
    this.getRoomsUrl = window.location.origin + "/api/rooms";
}

IndexPage.prototype.initIndexPage = function () {
    const roomList = document.querySelector(".room-list");

    fetch(indexPage.getRoomsUrl, {
        method: 'GET'
    }).then(res => res.json())
        .then(function (data) {
            for (let i = 0; i < data.roomIds.length; i++) {
                roomList.innerHTML +=
                    `<li class="room">
                        <button class="room-button" onclick="location.href = 'chess/' + ${data.roomIds[i]}">
                        ${data.roomIds[i]}
                        </button>
                    </li>`;
            }
        });
}

IndexPage.prototype.addNewRoom = function (newRoomId) {
    fetch(indexPage.postPiecesUrl + newRoomId, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    }).then(res => res.json())
        .then(data => {
            localStorage.setItem("pieces", JSON.stringify(data));
            location.href = 'chess/' + newRoomId;
        });
}