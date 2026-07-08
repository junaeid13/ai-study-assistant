function UploadForm({ 
    file,
    setFile,
    uploadFile,
    loading,
 }) {
    
    return (
        <div>
            <input
                type="file"
                accept=".pdf"
                onChange={(e) => setFile(e.target.files[0])}
            />
            <button 
                onClick={uploadFile} 
                disabled={loading}
            >
                {
                    loading 
                    ? "Processing..." 
                    : "Upload"
                }
            </button>
        </div>
    );
}
export default UploadForm;