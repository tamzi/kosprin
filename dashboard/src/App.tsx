
import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { CssBaseline, Container, Typography, Box, Grid, Paper, AppBar, Toolbar } from '@mui/material';

// Sample Dashboard components
const Home = () => (
    <Box>
        <Typography variant="h5" gutterBottom>Dashboard Overview</Typography>
        <Grid container spacing={3}>
            <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240 }}>
                    <Typography variant="h6">Recent Activity</Typography>
                    <Box sx={{ mt: 2 }}>
                        <Typography>User signups increased by 15% this week</Typography>
                        <Typography>Revenue up 12% from last month</Typography>
                        <Typography>New feature adoption rate: 34%</Typography>
                    </Box>
                </Paper>
            </Grid>
            <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: 240 }}>
                    <Typography variant="h6">Key Metrics</Typography>
                    <Box sx={{ mt: 2 }}>
                        <Typography>Active Users: 12,543</Typography>
                        <Typography>Conversion Rate: 3.2%</Typography>
                        <Typography>Avg. Session Duration: 4m 32s</Typography>
                    </Box>
                </Paper>
            </Grid>
            <Grid item xs={12}>
                <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column' }}>
                    <Typography variant="h6">Monthly Trends</Typography>
                    <Box sx={{ height: 300, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                        <Typography color="text.secondary">Chart visualization would appear here</Typography>
                    </Box>
                </Paper>
            </Grid>
        </Grid>
    </Box>
);

const Analytics = () => (
    <Box>
        <Typography variant="h5" gutterBottom>Analytics</Typography>
        <Paper sx={{ p: 2 }}>
            <Typography>Detailed analytics content would appear here</Typography>
        </Paper>
    </Box>
);

const Settings = () => (
    <Box>
        <Typography variant="h5" gutterBottom>Settings</Typography>
        <Paper sx={{ p: 2 }}>
            <Typography>Dashboard configuration options would appear here</Typography>
        </Paper>
    </Box>
);

const App: React.FC = () => (
    <BrowserRouter>
        <CssBaseline />
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Analytics Dashboard
                    </Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4, flexGrow: 1 }}>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/analytics" element={<Analytics />} />
                    <Route path="/settings" element={<Settings />} />
                </Routes>
            </Container>
            <Box component="footer" sx={{ p: 2, bgcolor: 'background.paper' }}>
                <Typography variant="body2" color="text.secondary" align="center">
                    Dashboard Demo © {new Date().getFullYear()}
                </Typography>
            </Box>
        </Box>
    </BrowserRouter>
);

export default App;
