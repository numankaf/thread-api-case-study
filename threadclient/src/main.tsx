import { PrimeReactProvider } from 'primereact/api';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { StompSessionProvider } from 'react-stomp-hooks';
import App from './App.tsx';
import { LayoutProvider } from './context/LayoutContext.tsx';
import { SocketDataProvider } from './context/SocketDataContext.tsx';
import { ToastProvider } from './context/ToastContext.tsx';
import './index.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <PrimeReactProvider>
      <LayoutProvider initialTheme="light">
        <ToastProvider>
          <StompSessionProvider url={import.meta.env.VITE_SOCKET_URL}>
            <SocketDataProvider>
              <App />
            </SocketDataProvider>
          </StompSessionProvider>
        </ToastProvider>
      </LayoutProvider>
    </PrimeReactProvider>
  </StrictMode>,
);
