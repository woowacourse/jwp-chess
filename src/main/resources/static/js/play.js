const templateQueen = color => `<div class="wooteco.chess-piece queen ${color}" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateKing = color => `<div class="wooteco.chess-piece king ${color}" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateBishop = color => `<div class="wooteco.chess-piece bishop ${color}" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateKnight = color => `<div class="wooteco.chess-piece knight ${color}" draggable="true" ondrag="onDrag())" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateRook = color => `<div class="wooteco.chess-piece rook ${color}" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templatePawn = color => `<div class="wooteco.chess-piece pawn ${color}" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`
const templateBlank = () => `<div class="wooteco.chess-piece" draggable="true" ondrag="onDrag(event)" ondrop="onDrop(event)" ondragstart="onDragStart(event)" ondragover="onDragOver(event)"></div>`

const createTemplate = (team, symbol) => {
    console.log(team, symbol);
    switch (symbol) {
        case 'p':
            if (team == 'WHITE')
                return templatePawn('white')
            else
                return templatePawn('black')
        case 'q':
            if (team == 'WHITE')
                return templateQueen('white')
            else
                return templateQueen('black')
        case 'k':
            if (team == 'WHITE')
                return templateKing('white')
            else
                return templateKing('black')
        case 'r':
            if (team == 'WHITE')
                return templateRook('white')
            else
                return templateRook('black')
        case 'b':
            if (team == 'WHITE')
                return templateBishop('white')
            else
                return templateBishop('black')
        case 'n':
            if (team == 'WHITE')
                return templateKnight('white')
            else
                return templateKnight('black')
    }
    return templateBlank();
}
const findChessCell = (x, y) => document.querySelector(`[data-x="${x - 1}"][data-y="${y - 1}"]`)

fetch("http://localhost:4567/loadBoard/" + {
{
    id
}
}).
then((response) => response.json()).then((board) => {
    console.dir(board);
    for (let i = 1; i < 9; i++) {
        for (let j = 1; j < 9; j++) {
            findChessCell(i, j).innerHTML = templateBlank();
        }
    }
    let units = board.units;
    for (let i = 0; i < units.length; i++) {
        let position = findChessCell(units[i].x, units[i].y);
        position.innerHTML = createTemplate(units[i].team, units[i].unit);
    }
})

function onDragStart(e) {
    console.log('dragstart')
    e.dataTransfer.setData('x', parseInt(e.target.parentElement.dataset.x) + 1)
    e.dataTransfer.setData('y', parseInt(e.target.parentElement.dataset.y) + 1)
}

function onDrag(e) {
    console.log('drag')
}

function onDragOver(e) {
    e.preventDefault()
    console.log('dragover')
}

function onDrop(e) {
    console.log(e.dataTransfer.getData('x'))
    console.log(e.dataTransfer.getData('y'))
    console.log(e.target.parentElement.dataset.x + 1)
    console.log(e.target.parentElement.dataset.y + 1)
    console.log('drop');

    let sourceX = e.dataTransfer.getData('x');
    let sourceY = e.dataTransfer.getData('y');
    let targetX = (parseInt(e.target.parentElement.dataset.x) + 1);
    let targetY = (parseInt(e.target.parentElement.dataset.y) + 1);

    fetch("http://localhost:4567/move/" + {
    {
        id
    }
},
    {
        method: 'POST', body
    :
        JSON.stringify({
            sourceX: e.dataTransfer.getData('x'),
            sourceY: e.dataTransfer.getData('y'),
            targetX: (parseInt(e.target.parentElement.dataset.x) + 1) + "",
            targetY: (parseInt(e.target.parentElement.dataset.y) + 1) + "",
        })
    }
).
    then(function (response) {
        if (response.ok) {
            findChessCell(targetX, targetY).innerHTML = findChessCell(sourceX, sourceY).innerHTML;
            findChessCell(sourceX, sourceY).innerHTML = templateBlank();
        }
    }).catch(function (error) {
        alert(error);
    });
}