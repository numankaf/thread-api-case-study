import { useContext } from 'react';
import { LayoutContext } from '../context/LayoutContext';

export const useLayout = () => {
  const loadedayoutContext = useContext(LayoutContext);

  if (!loadedayoutContext) {
    throw new Error('Layout context is not loaded');
  }

  return loadedayoutContext;
};
