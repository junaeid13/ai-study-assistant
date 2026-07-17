function QuizOption({
    options,
    selected,
    onClick
}) {
    return (
        <button
            onClick={onClick}
            style={{
                display: 'block',
                width: '100%',
                padding: '10px',
                marginTop: '10px',
                paddingLeft: '10px',
                backgroundColor: selected ? 'lightblue' : "gray",
                cursor: 'pointer',
        }}>{options}</button>
    );
}
export default QuizOption;