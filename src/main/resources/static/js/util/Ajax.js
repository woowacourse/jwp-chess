export class Ajax {
    static #URL = 'http://localhost:8080/'

    #chessGame

    constructor(chessGame) {
        this.#chessGame = chessGame
    }

    getURL(gameId) {
        return Ajax.#URL + "/game/" + gameId + "/"
    }

    async get(gameId, uri) {
        let result = await fetch(this.getURL(gameId) + uri)
        if (result.ok) return await result.json()

        throw await result.json()
    }

    async post(gameId, uri, data) {
        const option = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        }

        let result = await fetch(this.getURL(gameId) + uri, option)
        if (result.ok) return await result.json()

        throw await result.json()
    }

    async delete(gameId, uri, data) {
        const option = {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        }

        let result = await fetch(this.getURL(gameId) + uri, option)
        if (result.ok) return await result.json()

        throw await result.json()
    }

    async patch(gameId, uri, data) {
        const option = {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: data
        }

        let result = await fetch(this.getURL(gameId) + uri, option)
        if (result.ok) return await result.json()

        throw await result.json()
    }

}