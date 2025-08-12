import React, { useState, useEffect } from 'react';
import { Modal, Box, Typography, TextField, Button } from '@mui/material';

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 4,
};

function PrateleiraModal({ open, handleClose, handleSave, prateleiraInicial }) {
  // O estado inicial permanece o mesmo
  const [prateleira, setPrateleira] = useState({ codigo: '' });

  // useEffect ATUALIZADO para tratar valores nulos
  useEffect(() => {
    if (prateleiraInicial) {
      // Se estamos editando, preenchemos o estado garantindo que não haja valores nulos
      setPrateleira({
        id: prateleiraInicial.id,
        codigo: prateleiraInicial.codigo ?? ''
      });
    } else {
      // Se estamos criando uma nova, limpamos o formulário
      setPrateleira({ codigo: '' });
    }
  }, [prateleiraInicial, open]); // As dependências estão corretas

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPrateleira(prevState => ({ ...prevState, [name]: value }));
  };

  const onSave = () => {
    handleSave(prateleira);
  };

  return (
    <Modal open={open} onClose={handleClose}>
      <Box sx={style}>
        <Typography variant="h6" component="h2">
          {prateleira.id ? 'Editar Prateleira' : 'Adicionar Nova Prateleira'}
        </Typography>
        
        <TextField 
          margin="normal" 
          fullWidth 
          label="Código da Prateleira" 
          name="codigo" 
          value={prateleira.codigo} 
          onChange={handleChange} 
        />
        
        <Box mt={2} sx={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Button onClick={handleClose} sx={{ mr: 1 }}>Cancelar</Button>
          <Button onClick={onSave} variant="contained">Salvar</Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default PrateleiraModal;