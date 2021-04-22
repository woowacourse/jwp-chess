import {deleteRoom, makeRoom} from "./fetch.js"

const main = {
    init: function () {
        const _this = this;
        const $newGame = document.querySelector(".room-maker");
        $newGame.addEventListener("click", async () => {
            await _this.make()
        });
    },

    make: async function () {
        const newRoom = prompt("새로 생성할 방 이름을 입력하세요.");
        await makeRoom(newRoom);
    },
}

main.init();