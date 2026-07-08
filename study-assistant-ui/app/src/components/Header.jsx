function Header( {username, onLogout}) {

    return (
        <header
            style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                padding: "10px 20px",
            }}
        >
            <h1>AI Study Assistant</h1>
            <div>
                <span style={{ marginRight: "10px" }}>
                    Welcome, {username}
                </span>
                <button onClick={onLogout}>
                    Logout
                </button>
            </div>
        </header>
    );
}
export default Header;