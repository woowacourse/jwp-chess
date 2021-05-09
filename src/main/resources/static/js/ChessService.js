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

    async createRoom({roomName, password}) {
        return await fetch(`${this.baseUrl}/rooms`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify({roomName, password})
        });
    }

    async moveSourceToTarget(moveRequest, roomId) {
        return await fetch(`${this.baseUrl}/rooms/${roomId}/move`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify(moveRequest)
        }).then(response => response.json());
    }

    async loadPrevGame(roomId) {
        const response = await fetch(`${this.baseUrl}/rooms/${roomId}/previous`);
        return response.json();
    }

    async checkAllowedUser(roomId, {password}) {
        console.log(roomId, password)
        return await fetch(`${this.baseUrl}/rooms/${roomId}/password`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify({password})
        }).then(response => response.json());

    }

}