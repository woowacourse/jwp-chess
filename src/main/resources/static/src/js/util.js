function parseToJSON(data) {
    if (typeof data == "string") {
        data = JSON.parse(data);
    }
    return data;
}
