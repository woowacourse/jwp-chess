const fetchOption = (method, body) => ({
    method,
    body: body ? JSON.stringify(body) : null,
    headers: {
        'Content-Type': 'application/json; charset=UTF-8',
    },
});

export const API = Object.freeze({
    HTTP_METHOD: {
        GET: 'GET',
        POST: 'POST',
        PUT: 'PUT',
        PATCH: 'PATCH',
        DELETE: 'DELETE',
    },
});

export const HTTP_CLIENT = {
    get: path => fetch(path, fetchOption(API.HTTP_METHOD.GET, null)),
    post: (path, body) => fetch(path, fetchOption(API.HTTP_METHOD.POST, body)),
    put: (path, body) => fetch(path, fetchOption(API.HTTP_METHOD.PUT, body)),
    patch: (path, body) => fetch(path, fetchOption(API.HTTP_METHOD.PATCH, body)),
    delete: path => fetch(path, fetchOption(API.HTTP_METHOD.DELETE, null)),
}

export const PATH = {
    ROOM: '/api/rooms',
    CHESS: '/api/chess',
}

export const COOKIE = name => document.cookie
    .split("; ")
    .find(row => row.startsWith(name))
    .split("=")[1];
