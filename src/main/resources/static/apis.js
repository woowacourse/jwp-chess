const API_HOST = "http://localhost:8080"

const fetchAsGet = async (path) => {
    const response = await fetch(`${API_HOST}${path}`, {
        method: "GET"
    });

    return response.json();
}

const fetchAsPost = async (path, body) => {
    await fetch(`${API_HOST}${path}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });
}

const fetchRooms = async () => {
    return fetchAsGet("/rooms");
}

const fetchBoard = async (roomId) => {
    return fetchAsGet(`/rooms/${roomId}/board`);
}

const fetchCurrentTurn = async (roomId) => {
    return fetchAsGet(`/rooms/${roomId}/turn`);
}

const fetchScore = async (roomId) => {
    return fetchAsGet(`/rooms/${roomId}/score`);
}

const fetchWinner = async (roomId) => {
    return fetchAsGet(`/rooms/${roomId}/winner`);
}

const createRoom = async (title, password) => {
    await fetchAsPost(`/rooms`, {title, password});
}

const move = async (roomId, from, to) => {
    await fetchAsPost(`/rooms/${roomId}/move`, {from, to})
}
