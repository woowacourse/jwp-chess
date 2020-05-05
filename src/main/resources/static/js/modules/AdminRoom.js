import {EVENT_TYPE, KEY_TYPE} from "../../utils/constants.js";
import {listItemTemplate} from "../../utils/templates.js";

let currentTop = getCount();

function getCount() {
    const $lastRoomId = document.querySelector("#room-list").lastElementChild.innerText;

    if ($lastRoomId) {
        return parseInt($lastRoomId) + 1;
    }
    return 1;
}

function AdminRoom() {
    const $roomList = document.querySelector("#room-list");
    const $roomAddButton = document.querySelector('#room-add-btn');

    const onAddRoomHandler = event => {
        if (event.type !== EVENT_TYPE.CLICK && event.key !== KEY_TYPE.ENTER) {
            return;
        }

        event.preventDefault();
        $.ajax({
            type: 'get',
            url: '/add',
            data: {roomId: currentTop},
            error: function (request, status, error) {
                alert(request.status + "\n" + request.responseText + "\n" + error + "\n" + status);
            },
            success: function () {
                alert('방 추가!');
                $roomList.insertAdjacentHTML("beforeend", listItemTemplate(currentTop++));
            }
        });
    };

    const roomListHandler = event => {
        const $target = event.target;
        const roomId = $target.textContent.trim();
        if (roomId) {
            $.ajax({
                type: 'get',
                url: `/chess/${roomId}`,
                error: function (request, status, error) {
                    alert(request.status + "\n" + request.responseText + "\n" + error + "\n" + status);
                },
                success: function () {
                    window.location.href = `/chess/${roomId}`;
                }
            });
        }
        const isDeleteButton = $target.classList.contains("mdi-delete");
        if (isDeleteButton && confirm("정말로 삭제할거에요?")) {
            $target.closest(".list-item").remove();
        }
    };

    const initEventListeners = () => {
        $roomList.addEventListener(EVENT_TYPE.CLICK, roomListHandler);
        $roomAddButton.addEventListener(EVENT_TYPE.CLICK, onAddRoomHandler);
    };

    const init = () => {
        initEventListeners();
    };

    return {
        init
    };
}

const adminRoom = new AdminRoom();
adminRoom.init();
