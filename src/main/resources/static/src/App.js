const URLSearch = new URLSearchParams(location.search);
let source = null;
let target = null;

function create() {
}

function start() {
    fetch('/start', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(null)
    }).then(response => {
        if (!response.ok) {
            response.json()
                .then(body => alert(body.message));
            location.replace("/")
            return;
        }
        location.replace("/play");
    });
}

function end() {
    fetch('/end')
        .then(response => {
            if (response.ok) {
                response.json()
                    .then(body => alert(body.message));
                location.replace("/")
            }
        });
}

function selectBlock(id) {
    if (source == null) {
        source = id;
        return;
    }
    target = id;
    move(source, target)
}

function getGameId() {
    let pathName = location.pathname.split("/");
    return pathName[2];
}

function move(source, target) {
    const request = {
        source: source.id,
        target: target.id
    }

    reinitialize();

    fetch('/move/' + getGameId(), {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(request)
    })
        .then(response => {
            if (!response.ok) {
                response.json()
                    .then(body => alert(body.message));
                return;
            }
            location.replace("/play/" + getGameId());
        });
}

function deleteGame(id){

    let password = prompt("비밀번호를 입력해주세요.")
    const request = {
        password : password
    }

    fetch('/delete/' + id, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        },
        body: JSON.stringify(request)
    })
           .then(response => {
               if (!response.ok) {
                   response.json()
                       .then(body => alert(body.message));
                   return;
               }
               location.replace("/");
           });
}

function reinitialize() {
    source = null;
    target = null;
}

function status() {
    fetch('/status/' + getGameId(), {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                response.json()
                    .then(body => alert(body.message));
                return;
            }
            location.replace("/status/" + getGameId())
        });
}
