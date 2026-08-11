import KeyConcept from './KeyConcept';

function KeyConceptList({ keyConcepts }) {

    if (!keyConcepts || keyConcepts.length === 0) {
        return <p>No key concepts available.</p>;
    }

    return (
        <div style={{ padding: "20px"}}>
            <h2
                style={{
                    textAlign: 'center',
                    marginBottom: '20px',
                }}
            >
                Key Concepts
            </h2>
            <div
                style={{
                    display: 'grid',
                    gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))",
                    gap: "20px",
                    justifyItems: "center",
                }}
            >
                {keyConcepts.map((keyConcept) => (
                    <KeyConcept 
                        key={keyConcept.id} 
                        keyConcept={keyConcept} 
                    />
                ))}
            </div>
        </div>
    );
}

export default KeyConceptList;