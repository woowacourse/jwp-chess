const CreateRoom = () => {
    const [title, setTitle] = React.useState("");
    const [password, setPassword] = React.useState("");

    const handleTitleChange = (e) => {
        setTitle(e.target.value);
    }

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleCreateClick = () => {
        createRoom(title, password);
        // TODO: 생성된 방으로 리다이렉트
    }

    return (
        <React.Fragment>
            <TextField type="text" onChange={handleTitleChange} />
            <TextField type="password" onChange={handlePasswordChange} />

            <Button onClick={handleCreateClick}> 방 생성하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<CreateRoom/>, document.querySelector('#root'));