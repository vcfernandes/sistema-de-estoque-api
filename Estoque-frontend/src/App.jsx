import { useState } from 'react';
import axios from 'axios';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Layout from './components/Layout';
import ProdutosPage from './pages/ProdutosPage';
import PrateleirasPage from './pages/PrateleirasPage';

function App() {
  const [token, setToken] = useState(localStorage.getItem('token'));

  const handleLogin = async (username, password) => {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', { username, password });
      const newToken = response.data.token;
      localStorage.setItem('token', newToken);
      setToken(newToken);
    } catch (error) {
      console.error("Falha na autenticação", error);
      throw error;
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    setToken(null);
  };

  if (!token) {
    return <Login onLogin={handleLogin} />;
  }
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout onLogout={handleLogout} />}>
          <Route index element={<Navigate to="/produtos" replace />} />
          <Route path="produtos" element={<ProdutosPage />} />
          <Route path="prateleiras" element={<PrateleirasPage />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;