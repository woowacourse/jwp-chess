import {ROOM_ID, URL} from "../constant/constants.js";

export const move = async (source, target) => {
    try {
        const option = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: `{
                    "source": "${source}",
                    "target": "${target}"
                }`
        }

        const result = await fetch(URL.MOVE(ROOM_ID()), option)
        return await result.json();
    } catch (e) {
        alert(e)
    }
}

export const create_room = async room_name => {
    const option = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    }

    try {
        const result = await fetch(URL.CREATE_ROOM(room_name), option)
        return await result.json()
    } catch (e) {
        alert(e)
    }
}

export const all_rooms = async () => {
    try {
        const result = await fetch(URL.ALL_ROOMS())
        return await result.json()
    } catch (e) {
        alert(e)
    }
}

export const game_info = async () => {
    try {
        const result = await fetch(URL.GAME_INFO(ROOM_ID()))
        return await result.json();
    } catch (e) {
        alert(e)
    }
}

export const game_status = async () => {
    try {
        const result = await fetch(URL.STATUS(ROOM_ID()))
        return await result.json();
    } catch (e) {
        alert(e)
    }
}

export const end = async game_id => {
    const option = {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        }
    }

    try {
        const result = await fetch(URL.END(game_id), option)
        return await result.json()
    } catch (e) {
        alert(e)
    }
}