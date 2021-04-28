import {RoomView} from "../view/roomView.js";
import {create_room} from "../../api/api.js";
import {$} from "../../util/util.js";
import {SELECTOR} from "../../constant/constants.js";

export class RoomController {

    #roomView = new RoomView()

    init() {
        this.#roomView.render()
        this.#createRoomHandler()
        this.#enterRoomHandler()
    }

    async #createRoomHandler() {
        $(SELECTOR.START_BUTTON).addEventListener('click', async e => {
            let room_name = prompt("방 이름을 입력해 주세요")
            if (room_name === "" || room_name === undefined) {
                return
            }

            let room = await create_room(room_name)
            console.log(room)
            location.href = `/game.html?game_id=${room.roomId}`
        })
    }

    async #enterRoomHandler() {
        $(SELECTOR.ROOMS).addEventListener('click', async e => {
            if (e.target !== undefined && e.target.nodeName === "LI") {
                let li = e.target
                let roomId = li.id

                location.href = `/game.html?game_id=${roomId}`
            }
        })
    }

}