export const SELECTOR = {
    START_BUTTON: "#start_button",
    ROOMS: "#rooms"
}

export const URL = {
    MOVE: room_id => `http://localhost:8080/games/${room_id}/position`,
    CREATE_ROOM: room_name => `http://localhost:8080/rooms/${room_name}`,
    ALL_ROOMS: () => `http://localhost:8080/rooms`,
    GAME_INFO: room_id => `http://localhost:8080/games/${room_id}`,
    STATUS: room_id => `http://localhost:8080/games/${room_id}/status`,
    END: room_id => `http://localhost:8080/games/${room_id}/end`
}

export const ROOM_ID = () => {
    let params = new URLSearchParams(location.search);
    return params.get("game_id")
}