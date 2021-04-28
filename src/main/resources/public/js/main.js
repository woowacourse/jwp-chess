let main = {
    init: function () {
        this.showList();
        const destroyBtn = document.querySelector('.room_list')
        destroyBtn.addEventListener('click', this.deleteRoom);
    },
    showList: function () {
        fetch("/rooms").then(res => {
            return res.json();
        }).then(function (room_ids) {
            const room_list = document.querySelector('.room_list')
            for (let room of room_ids) {
                room_list.insertAdjacentHTML('beforeend', renderRoomTemplate(room.roomId, room.roomName));
            }
        });

        function renderRoomTemplate(id, name) {
            return `<div class="room">
                            <a id="${id}" href="/rooms/${id}">방 번호 : ${id} <br> 방 제목 : ${name}</a>
                            <button class="destroy"></button>
                    </div>`;
        }
    },
    deleteRoom(event) {
        if (event.target.classList.contains('destroy')) {
            const roomId = event.target.parentNode.querySelector('a').id;
            fetch("/rooms/" + roomId, {
                method: 'delete'
            }).then(res => {
                if (res.status === 200) {
                    window.location.href = "/";
                }
            });
        }
    }
}

main.init();

