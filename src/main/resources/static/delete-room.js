const DeleteRoom = () => {
    const [password, setPassword] = React.useState("");

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
    }

    const handleDeleteClick = async () => {
        const {id} = parseQueryString();

        try {
            await deleteRoom(id, password);
        } catch (e) {
            alert(e.message);
        }
    }

    return (
        <React.Fragment>
            <TextField type="password" onChange={handlePasswordChange}/>
            <Button onClick={handleDeleteClick}> 방 제거하기 </Button>
        </React.Fragment>
    );
};

ReactDOM.render(<DeleteRoom/>, document.querySelector('#root'));