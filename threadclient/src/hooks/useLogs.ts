import { useContext } from 'react';
import { LogContext } from '../context/LogContext';

export const useLogs = () => {
  const context = useContext(LogContext);
  if (!context) {
    throw new Error('useLogs must be used within a LogProvider');
  }
  return context;
};
