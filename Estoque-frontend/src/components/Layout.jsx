import React from 'react';
import { Link, Outlet } from 'react-router-dom';
import { 
  AppBar, Toolbar, Typography, Drawer, List, 
  ListItem, ListItemButton, ListItemText, CssBaseline, Box 
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
          <button onClick={onLogout} style={{color: 'white', background: 'none', border: 'none', cursor: 'pointer', fontSize: '1rem'}}>Sair</button>
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
            {/* 2. Use ListItem e ListItemButton aninhados para links */}
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

          </List>
        </Box>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <Outlet /> 
      </Box>
    </Box>
  );
}

export default Layout;