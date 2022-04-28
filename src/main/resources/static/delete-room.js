const CreateRoom = () => {
    const [password, setPassword] = React.useState("");

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleDeleteClick = async () => {
        const {id} = parseQueryString();
        await deleteRoom(id, password);
        console.log(id, password)
        // TODO: 생성된 방으로 리다이렉트
    }

    return (
        <React.Fragment>
            <TextField type="password" onChange={handlePasswordChange}/>
            <Button onClick={handleDeleteClick}> 방 제거하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<CreateRoom/>, document.querySelector('#root'));