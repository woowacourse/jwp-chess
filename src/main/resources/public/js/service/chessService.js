export async function getGridAndPiecesByRoomName(roomName) {
    return await axios({
        method: 'get',
        url: `/grid/${roomName}`,
    });
}

export async function gameStateByGridId(command, gridId) {
    return await axios({
        method: 'post',
        url: `/grid/${gridId}/state`,
        data: {
            command,
        }

    });
}

export async function finishGameByGridId(gridId) {
    return await axios({
        method: 'post',
        url: `/grid/${gridId}/finish`
    });
}

export async function movePiece(piecesDto, gridDto, sourcePosition,
    targetPosition) {
    return await axios({
        method: 'post',
        url: '/move',
        data: {
            piecesDto,
            sourcePosition,
            targetPosition,
            gridDto
        }
    });
}

export async function getRooms() {
    return await axios({
        method: 'get',
        url: `/room`
    });
}