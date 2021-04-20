export default class ChessService {
    constructor() {
        this.baseUrl = "http://localhost:8080";
        this.headers = {
            "Content-Type": "application/json"
        };
    }

    async moveSourceToTarget({start, destination}) {
        return await fetch(`${this.baseUrl}/move`, {
            method: "POST",
            headers: this.headers,
            body: JSON.stringify({start, destination})
        }).then(response => response.json());
    }

    async startGame() {
        const response = await fetch(`${this.baseUrl}/startNewGame`);
        return response.json();
    }

    async loadPrevGame() {
        const response = await fetch(`${this.baseUrl}/loadPrevGame`);
        return response.json();
    }

}