import { PrimeReactProvider } from 'primereact/api';
import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import App from './App.tsx';
import { LayoutProvider } from './context/LayoutContext.tsx';
import { ToastProvider } from './context/ToastContext.tsx';
import './index.css';

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <PrimeReactProvider>
      <LayoutProvider initialTheme="light">
        <ToastProvider>
          <App />
        </ToastProvider>
      </LayoutProvider>
    </PrimeReactProvider>
  </StrictMode>,
);
