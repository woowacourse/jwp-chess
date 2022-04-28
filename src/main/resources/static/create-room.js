const CreateRoom = () => {
    const [title, setTitle] = React.useState("");
    const [password, setPassword] = React.useState("");

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleCreateClick = async () => {
        try {
            const room = await createRoom(title, password);
            location.href = `/game.html?id=${room.id}`;
        } catch (e) {
            alert(e.message);
        }
    }

    return (
        <React.Fragment>
            <TextField type="text" onChange={handleTitleChange}/>
            <TextField type="password" onChange={handlePasswordChange}/>

            <Button onClick={handleCreateClick}> 방 생성하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<CreateRoom/>, document.querySelector('#root'));