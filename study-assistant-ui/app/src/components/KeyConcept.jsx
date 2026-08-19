function KeyConcept({ keyConcept }) {
    return (
        <div 
            style={{
                border: '1px solid #ccc',
                borderRadius: '8px',
                padding: '20px',
                margin: '10px',
                width: '100%',
                maxWidth: '800px',
                boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',  
            }}
        >
            <h3>{keyConcept.concept}</h3>
            <p
                style={{
                    lineHeight: '1.6',
                    whiteSpace: 'pre-wrap',
                }}
            >{keyConcept.explanation}</p>
        </div>
    );
    export default KeyConcept;
}