import {all_rooms} from "../../api/api.js";
import {SELECTOR} from "../../constant/constants.js";
import {roomInfoTemplate} from "../templates/templates.js";
import {$, parseDomFromString} from "../../util/util.js"

export class RoomView {

    async render() {
        let rooms = await all_rooms()

        for (let room of rooms) {
            $(SELECTOR.ROOMS).appendChild(
                parseDomFromString(roomInfoTemplate(room.roomId, room.roomName))
            )
        }
    }

}