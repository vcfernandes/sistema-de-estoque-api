import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { 
  Typography, CircularProgress, Alert, Table, TableBody, TableCell, TableContainer,
  TableHead, TableRow, Paper, IconButton, Box, TextField, Button
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import ProdutoModal from '../components/ProdutoModal';

function ProdutosPage() {
  const [produtos, setProdutos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [termoBusca, setTermoBusca] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [produtoSelecionado, setProdutoSelecionado] = useState(null);
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchProducts = async () => {
      if (!token) {
        setError("Token não encontrado.");
        setLoading(false);
        return;
      }
      setLoading(true);
      try {
        const response = await axios.get('http://localhost:8080/api/produtos', {
          headers: { 'Authorization': `Bearer ${token}` }
        });
        setProdutos(response.data);
      } catch (err) {
        setError('Erro ao carregar produtos.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchProducts();
  }, [token]);

  const handleOpenModal = (produto = null) => {
    setProdutoSelecionado(produto);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setProdutoSelecionado(null);
  };

  const handleSaveProduto = async (produtoParaSalvar) => {
    try {
      const config = { headers: { 'Authorization': `Bearer ${token}` } };
      if (produtoParaSalvar.id) {
        await axios.put(`http://localhost:8080/api/produtos/${produtoParaSalvar.id}`, produtoParaSalvar, config);
      } else {
        await axios.post('http://localhost:8080/api/produtos', produtoParaSalvar, config);
      }
      // Após salvar, busca os produtos novamente para atualizar a lista
      const response = await axios.get('http://localhost:8080/api/produtos', config);
      setProdutos(response.data);
      handleCloseModal();
    } catch (err) {
      setError(err.response?.data?.erro || 'Erro ao salvar produto.');
      console.error(err);
    }
  };

  const handleDeleteProduto = async (produtoId) => {
    if (window.confirm("Tem certeza que deseja deletar este produto?")) {
      try {
        const config = { headers: { 'Authorization': `Bearer ${token}` } };
        await axios.delete(`http://localhost:8080/api/produtos/${produtoId}`, config);
        // Atualiza a lista removendo o item deletado, sem precisar de uma nova chamada à API
        setProdutos(produtosAtuais => produtosAtuais.filter(p => p.id !== produtoId));
      } catch (err) {
        setError(err.response?.data?.erro || 'Erro ao deletar produto.');
        console.error(err);
      }
    }
  };

  const produtosFiltrados = produtos.filter(produto => 
    produto.nome.toLowerCase().includes(termoBusca.toLowerCase())
  );

  if (loading) return <CircularProgress />;
  if (error) return <Alert severity="error" onClose={() => setError('')}>{error}</Alert>;

  return (
    <div>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" gutterBottom component="div">
          Meus Produtos
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenModal()}
        >
          Novo Produto
        </Button>
      </Box>

      <Box sx={{ mb: 2 }}>
        <TextField 
          fullWidth
          label="Pesquisar por nome..."
          variant="outlined"
          value={termoBusca}
          onChange={(e) => setTermoBusca(e.target.value)}
        />
      </Box>
      
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }}>
          <TableHead>
            <TableRow>
              <TableCell>Nome do Produto</TableCell>
              <TableCell align="right">Preço</TableCell>
              <TableCell align="right">Estoque</TableCell>
              <TableCell align="center">Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {produtosFiltrados.map((produto) => (
              <TableRow key={produto.id}>
                <TableCell>{produto.nome}</TableCell>
                <TableCell align="right">R$ {produto.preco.toFixed(2)}</TableCell>
                <TableCell align="right">{produto.quantidadeEmEstoque}</TableCell>
                <TableCell align="center">
                  <IconButton aria-label="edit" onClick={() => handleOpenModal(produto)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton aria-label="delete" onClick={() => handleDeleteProduto(produto.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <ProdutoModal 
        open={openModal} 
        handleClose={handleCloseModal}
        handleSave={handleSaveProduto}
        produtoInicial={produtoSelecionado}
      />
    </div>
  );
}

export default ProdutosPage;