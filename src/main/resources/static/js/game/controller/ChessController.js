import {PieceFactory} from "../domain/piece/PieceFactory.js";
import {game_info, game_status, move} from "../../api/api.js";

export class ChessController {
    #chessGame
    #turn
    #selected

    constructor(chessGame) {
        this.#chessGame = chessGame
    }

    async run() {
        await this.#load()
        this.moveEventHandler()
    }

    async #load() {
        let result = await game_info();

        await this.#printGameStatus()
        this.#resultHandler(result)
    }

    async #move(e) {
        if (this.#selected && e.target && ![...e.target.classList].includes(this.#turn)) {
            let source = this.#selected.id
            let target = e.target.id

            let result = await move(source, target)

            this.#resultHandler(result)
            await this.#printGameStatus()

            this.#selected = undefined
        }
    }

    #finished(winner) {
        this.#chessGame.setWinner(winner)
    }

    async #printGameStatus() {
        let result = await game_status()
        this.#chessGame.setStatus(result)
    }


    #resultHandler(result) {
        let pieces = PieceFactory.getPiecesByPieceDtos(result)
        this.#chessGame.setPieces(pieces)

        if (result.status === 'running') {
            this.#chessGame.setTurn(result.turn)
            this.#turn = result.turn
        }

        if (result.status !== 'running') {
            this.#finished(this.#calculateWinner(result))
        }

    }

    #calculateWinner(result) {
        if (result.status === 'blackWin') return "black"
        if (result.status === 'whiteWin') return "white"
        return this.#chessGame.getWinnerByScore
    }

    async #selectPiece(e) {
        if (e.target && [...e.target.classList].includes(this.#turn)) {
            if (this.#selected === e.target) {
                e.target.classList.remove('selected')
                this.#selected = undefined

                return
            }

            if (this.#selected !== undefined) {
                this.#selected.classList.remove('selected')
            }

            this.#selected = e.target
            e.target.classList.add('selected')
            return
        }
    }

    moveEventHandler() {
        document.getElementById('chess_board')
            .addEventListener('click', async e => {
                await this.#selectPiece(e)
                await this.#move(e)
            })
    }

}