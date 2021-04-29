export default class ChessService {
    constructor() {
        this.baseUrl = "http://localhost:8080/game";
        this.headers = {
            "Content-Type": "application/json"
        };
    }

    async moveSourceToTarget(moveRequest, roomId) {
        return await fetch(`${this.baseUrl}/${roomId}/move`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify(moveRequest)
        }).then(response => response.json());
    }

    async startGame(roomId) {
        return await fetch(`${this.baseUrl}/${roomId}`, {
            method: "POST",
            headers: this.headers
        }).then(response => response.json());
    }

    async loadPrevGame(roomId) {
        const response = await fetch(`${this.baseUrl}/${roomId}/previous`);
        return response.json();
    }

}