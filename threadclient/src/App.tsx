import { createBrowserRouter, Navigate, RouterProvider } from 'react-router';
import './App.css';
import AppLayout from './layout/AppLayout';
import DashboardPage from './pages/DashboardPage';
import DocsPage from './pages/DocsPage';
import LogsPage from './pages/LogsPage';
import './styles/main.css';

function App() {
  const router = createBrowserRouter([
    {
      element: <AppLayout />,
      children: [
        {
          path: '/dashboard',
          element: <DashboardPage />,
        },
        {
          path: '/logs',
          element: <LogsPage />,
        },
        {
          path: '/docs',
          element: <DocsPage />,
        },
      ],
    },
    {
      path: '*',
      element: <Navigate to="/dashboard" replace={true} />,
    },
  ]);
  return <RouterProvider router={router} />;
}

export default App;
