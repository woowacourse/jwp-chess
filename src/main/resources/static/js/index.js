import {deleteRoom, makeRoom} from "./fetch.js"

const main = {
    init: function () {
        const _this = this;
        const $newGame = document.querySelector(".room-maker");
        $newGame.addEventListener("click", async () => {
            await _this.make()
        });

        const $deleteGames = document.querySelectorAll(".room-delete");
        $deleteGames.forEach(game => game.addEventListener("click", async ({target}) => {
            const gameId = target.closest(".room").id;
            await deleteRoom(gameId);
        }));
    },

    make: async function () {
        const newRoom = prompt("새로 생성할 방 이름을 입력하세요.");
        await makeRoom(newRoom);
    },
}

main.init();