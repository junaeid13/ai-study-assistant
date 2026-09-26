function ContentGenerationSection({
  title,
  buttonText,
  loadingText,
  loading,
  error,
  onGenerate,
  children,
}) {
  return (
    <>
      <h2>{title}</h2>

      <button
        onClick={onGenerate}
        disabled={loading}
        style={{ marginBottom: "20px" }}
      >
        {loading ? "Generating..." : buttonText}
      </button>

      {loading && <p>{loadingText}</p>}

      {error && (
        <p style={{ color: "red" }}>
          {error}
        </p>
      )}

      {!loading && !error && children}
    </>
  );
}

export default ContentGenerationSection;