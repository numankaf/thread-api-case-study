import { useContext } from 'react';
import { SocketDataContext } from '../context/SocketDataContext';

export const useSocketData = () => {
  const context = useContext(SocketDataContext);
  if (!context) {
    throw new Error('useSocketData must be used within a SocketDataProvider');
  }
  return context;
};
