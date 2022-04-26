let first = '';

const urls = location.href.split('/');
const gameId = urls[urls.length - 1];
const moveUrl = `/game/${gameId}/move`;

const clickEvent = (e) => {
    const target = e.target;
    if (first === '' && target.src.indexOf('NONE_EMPTY') !== -1) {
        return;
    }

    if (first === '') {
        first = target.id;
        return;
    }

    fetch(moveUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            from: first,
            to: target.id
        })
    }).then(res =>res.json())
        .then(json => {
            if (json.ok !== undefined && json.ok === false) {
                alert(json.message);
                return;
            }

            const from = json.from;
            const to = json.to;

            document.getElementById(to).src = document.getElementById(from).src;
            document.getElementById(from).src = '/images/NONE_EMPTY.png';
        });

    first = '';
};

document.querySelectorAll('.chess-ui > div')
    .forEach(e => e.addEventListener('click', clickEvent));
