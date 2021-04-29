export {getFetch, postFetch, deleteFetch}
const BASE_PORT = "8080";
const BASE_URL = `http://localhost:${BASE_PORT}`;

function getFetch(url) {
    return fetch(`${BASE_URL}${url}`).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    });
}

function postFetch(url, body = {}) {
    return fetch(`${BASE_URL}${url}`, {
        method: "post",
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    })
}

function deleteFetch(url) {
    return fetch(`${BASE_URL}${url}`, {
        method: "delete",
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data
    })
}

function exceptionHandling(errorPromise) {
    errorPromise.then(data => {
        alert(data.errorMsg);
    })
}