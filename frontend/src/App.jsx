import React, { useState } from 'react';

function App() {
  const [text, setText] = useState('');
  const [results, setResults] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleGenerate = async () => {
    if (!text.trim()) return;
    setLoading(true);
    setResults(null);

    try {
      const response = await fetch('http://localhost:8080/campaign/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ sourceText: text })
      });

      if (!response.ok) {
        const errText = await response.text();
        throw new Error(errText);
      }

      const data = await response.json();
      setResults(data);
    } catch (error) {
      console.error(error);
      alert('System Error: ' + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
      <h1>Autonomous Content Factory</h1>
      <h3>PAste product descirption here to generate marketing content.</h3>

      <div style={{ marginBottom: '20px' }}>
        <textarea
          value={text}
          onChange={(e) => setText(e.target.value)}
          rows="10"
          placeholder="Paste your  text here..."
          style={{ width: '100%' }}
        />
        <br />
        <button
          onClick={handleGenerate}
          disabled={loading}
          style={{ marginTop: '10px', padding: '10px 20px', fontSize: '16px' }}
        >
          {loading ? 'Generating...' : 'Generate'}
        </button>
      </div>

      {
        results && (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
            <h2>Final Review View</h2>
            <h3>Your content is ready for marketing</h3>

            <div style={{ border: '1px solid #aaa', padding: '15px' }}>
              <h3>Blog Post</h3>
              <div style={{ whiteSpace: 'pre-wrap' }}>{results.blog}</div>
            </div>

            <div style={{ border: '1px solid #aaa', padding: '15px' }}>
              <h3>Twitter/X Thread</h3>
              <div style={{ whiteSpace: 'pre-wrap' }}>{results.social}</div>
            </div>

            <div style={{ border: '1px solid #aaa', padding: '15px' }}>
              <h3>Email</h3>
              <div style={{ whiteSpace: 'pre-wrap' }}>{results.email}</div>
            </div>
          </div>
        )
      }
    </div>
  );
}

export default App;
