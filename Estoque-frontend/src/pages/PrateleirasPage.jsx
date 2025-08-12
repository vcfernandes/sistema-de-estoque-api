import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { 
  Typography, CircularProgress, Alert, IconButton, Box, Button, 
  Accordion, AccordionSummary, AccordionDetails, List, ListItem, ListItemText 
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import PrateleiraModal from '../components/PrateleiraModal';

function PrateleirasPage() {
  const [prateleiras, setPrateleiras] = useState([]);
  const [localizacoes, setLocalizacoes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [openModal, setOpenModal] = useState(false);
  const [prateleiraSelecionada, setPrateleiraSelecionada] = useState(null);
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchData = async () => {
      if (!token) {
        setError("Token não encontrado.");
        setLoading(false);
        return;
      }
      setLoading(true);
      try {
        const config = { headers: { 'Authorization': `Bearer ${token}` } };
        const [prateleirasRes, localizacoesRes] = await Promise.all([
          axios.get('http://localhost:8080/api/prateleiras', config),
          axios.get('http://localhost:8080/api/localizacoes', config)
        ]);
        setPrateleiras(prateleirasRes.data);
        setLocalizacoes(localizacoesRes.data);
      } catch (err) {
        setError('Erro ao carregar dados da página.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [token]);

  const refreshData = async () => {
    try {
      const config = { headers: { 'Authorization': `Bearer ${token}` } };
      const [prateleirasRes, localizacoesRes] = await Promise.all([
        axios.get('http://localhost:8080/api/prateleiras', config),
        axios.get('http://localhost:8080/api/localizacoes', config)
      ]);
      setPrateleiras(prateleirasRes.data);
      setLocalizacoes(localizacoesRes.data);
    } catch (err) {
      setError('Erro ao recarregar dados.');
    }
  };

  const handleOpenModal = (prateleira = null) => {
    setPrateleiraSelecionada(prateleira);
    setOpenModal(true);
  };

  const handleCloseModal = () => {
    setOpenModal(false);
    setPrateleiraSelecionada(null);
  };

  const handleSavePrateleira = async (prateleiraParaSalvar) => {
    try {
      const config = { headers: { 'Authorization': `Bearer ${token}` } };
      if (prateleiraParaSalvar.id) {
        await axios.put(`http://localhost:8080/api/prateleiras/${prateleiraParaSalvar.id}`, prateleiraParaSalvar, config);
      } else {
        await axios.post('http://localhost:8080/api/prateleiras', prateleiraParaSalvar, config);
      }
      refreshData();
      handleCloseModal();
    } catch (err) {
      setError(err.response?.data?.erro || 'Erro ao salvar prateleira.');
      console.error(err);
    }
  };

  const handleDeletePrateleira = async (prateleiraId) => {
    if (window.confirm("Tem certeza que deseja deletar esta prateleira?")) {
      try {
        const config = { headers: { 'Authorization': `Bearer ${token}` } };
        await axios.delete(`http://localhost:8080/api/prateleiras/${prateleiraId}`, config);
        refreshData();
      } catch (err) {
        setError(err.response?.data?.erro || 'Erro ao deletar prateleira.');
        console.error(err);
      }
    }
  };

  if (loading) return <CircularProgress />;
  if (error) return <Alert severity="error" onClose={() => setError('')}>{error}</Alert>;

  return (
    <div>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" gutterBottom component="div">
          Minhas Prateleiras
        </Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenModal()}
        >
          Nova Prateleira
        </Button>
      </Box>

      <div>
        {prateleiras.map(prateleira => {
          const produtosNestaPrateleira = localizacoes.filter(
            loc => loc.prateleira.id === prateleira.id
          );

          return (
            <Accordion key={prateleira.id}>
              <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                <Box sx={{ display: 'flex', width: '100%', alignItems: 'center' }}>
                  <Typography sx={{ flexGrow: 1 }}>{prateleira.codigo}</Typography>
                  <IconButton sx={{ mr: 1 }} aria-label="edit" onClick={(e) => { e.stopPropagation(); handleOpenModal(prateleira); }}>
                    <EditIcon />
                  </IconButton>
                  <IconButton aria-label="delete" onClick={(e) => { e.stopPropagation(); handleDeletePrateleira(prateleira.id); }}>
                    <DeleteIcon />
                  </IconButton>
                </Box>
              </AccordionSummary>
              <AccordionDetails>
                {produtosNestaPrateleira.length > 0 ? (
                  <List>
                    {produtosNestaPrateleira.map(loc => (
                      <ListItem key={loc.id}>
                        <ListItemText 
                          primary={loc.produto.nome}
                          secondary={`Posição: Linha ${loc.linha}, Coluna ${loc.coluna}`}
                        />
                      </ListItem>
                    ))}
                  </List>
                ) : (
                  <Typography>Nenhum produto alocado nesta prateleira.</Typography>
                )}
              </AccordionDetails>
            </Accordion>
          );
        })}
      </div>

      <PrateleiraModal 
        open={openModal}
        handleClose={handleCloseModal}
        handleSave={handleSavePrateleira}
        prateleiraInicial={prateleiraSelecionada}
      />
    </div>
  );
}

export default PrateleirasPage;