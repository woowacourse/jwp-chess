let main = {
    init: function () {
        this.showList();
    },
    showList: function () {
        const roomId = document.querySelector('#enterRoomId');

        fetch("/list").then(res => {
            return res.json();
        }).then(function (room_ids) {
            const room_list = document.querySelector('.room_list')
            for (let room of room_ids) {
                room_list.insertAdjacentHTML('beforeend', renderRoomTemplate(room.roomId, room.roomName));
            }
        });

        function renderRoomTemplate(id, name) {
            return ` <div class="room">
                        <a href="/enter/${id}">방 번호 : ${id} <br> 방 제목 : ${name}</a>
                    </div>`;
        }
    },
}

main.init();

