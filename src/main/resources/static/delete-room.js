const CreateRoom = () => {
    const [password, setPassword] = React.useState("");

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleDeleteClick = async () => {
        const {id} = parseQueryString();
        await deleteRoom(id, password);

        location.href = "/";
    }

    return (
        <React.Fragment>
            <TextField type="password" onChange={handlePasswordChange}/>
            <Button onClick={handleDeleteClick}> 방 제거하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<CreateRoom/>, document.querySelector('#root'));