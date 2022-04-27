async function handlingException(response) {
    if (response.ok) {
        return response;
    }
    const error = await response.json();
    throw Error(error.message);
}
