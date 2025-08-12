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

function ProdutoModal({ open, handleClose, handleSave, produtoInicial }) {
  // O estado inicial permanece o mesmo
  const [produto, setProduto] = useState({ nome: '', descricao: '', preco: '' });

  // useEffect ATUALIZADO para tratar valores nulos
  useEffect(() => {
    if (produtoInicial) {
      // Se estamos editando, preenchemos o estado garantindo que não haja valores nulos
      setProduto({
        id: produtoInicial.id,
        nome: produtoInicial.nome ?? '',
        descricao: produtoInicial.descricao ?? '',
        preco: produtoInicial.preco ?? ''
      });
    } else {
      // Se estamos criando um novo, limpamos o formulário
      setProduto({ nome: '', descricao: '', preco: '' });
    }
  }, [produtoInicial, open]); // As dependências estão corretas

  const handleChange = (e) => {
    const { name, value } = e.target;
    setProduto(prevState => ({ ...prevState, [name]: value }));
  };

  const onSave = () => {
    handleSave(produto);
  };

  return (
    <Modal
      open={open}
      onClose={handleClose}
    >
      <Box sx={style}>
        <Typography variant="h6" component="h2">
          {produto.id ? 'Editar Produto' : 'Adicionar Novo Produto'}
        </Typography>
        
        <TextField margin="normal" fullWidth label="Nome do Produto" name="nome" value={produto.nome} onChange={handleChange} />
        <TextField margin="normal" fullWidth label="Descrição" name="descricao" value={produto.descricao} onChange={handleChange} />
        <TextField margin="normal" fullWidth label="Preço" name="preco" type="number" value={produto.preco} onChange={handleChange} />
        
        <Box mt={2} sx={{ display: 'flex', justifyContent: 'flex-end' }}>
          <Button onClick={handleClose} sx={{ mr: 1 }}>Cancelar</Button>
          <Button onClick={onSave} variant="contained">Salvar</Button>
        </Box>
      </Box>
    </Modal>
  );
}

export default ProdutoModal;