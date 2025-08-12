import React from 'react';
import { Link, Outlet } from 'react-router-dom';
import { 
  AppBar, Toolbar, Typography, Drawer, List, 
  ListItem, ListItemButton, ListItemText, CssBaseline, Box, Button 
} from '@mui/material';

const drawerWidth = 240;

function Layout({ onLogout }) {
  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
        <Toolbar>
          <Typography variant="h6" noWrap component="div" sx={{ flexGrow: 1 }}>
            Sistema de Estoque
          </Typography>
          <Button color="inherit" onClick={onLogout}>
            Sair
          </Button>
        </Toolbar>
      </AppBar>
      
      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
        }}
      >
        <Toolbar />
        <Box sx={{ overflow: 'auto' }}>
          <List>
            <ListItem disablePadding>
              <ListItemButton component={Link} to="/produtos">
                <ListItemText primary="Produtos" />
              </ListItemButton>
            </ListItem>
            <ListItem disablePadding>
              <ListItemButton component={Link} to="/prateleiras">
                <ListItemText primary="Prateleiras" />
              </ListItemButton>
            </ListItem>
            {/* Adicionar mais links (Transações, Usuários, etc.) aqui */}
          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        {/* O Outlet renderiza o componente da rota atual (ex: ProdutosPage) */}
        <Outlet /> 
      </Box>
    </Box>
  );
}

export default Layout;