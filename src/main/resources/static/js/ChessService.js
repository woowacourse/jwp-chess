export default class ChessService {
    constructor() {
        this.baseUrl = "http://localhost:8080";
        this.headers = {
            "Content-Type": "application/json"
        };
    }

    async showRooms() {
        const response = await fetch(`${this.baseUrl}/rooms`);
        return response.json();
    }

    async createRoom({roomName}) {
        return await fetch(`${this.baseUrl}/rooms`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify({roomName})
        }).then(response => response.json());
    }

    async moveSourceToTarget(moveRequest, roomId) {
        return await fetch(`${this.baseUrl}/rooms/${roomId}/move`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify(moveRequest)
        }).then(response => response.json());
    }

    async startGame(roomId) {
        return await fetch(`${this.baseUrl}/rooms/${roomId}`, {
            method: "POST",
            headers: this.headers
        }).then(response => response.json());
    }

    async loadPrevGame(roomId) {
        const response = await fetch(`${this.baseUrl}/rooms/${roomId}/previous`);
        return response.json();
    }

}