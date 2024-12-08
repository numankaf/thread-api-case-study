import { PrimeReactContext } from 'primereact/api';
import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useState,
} from 'react';

interface LayoutContextType {
  theme: string;
  handleChangeTheme(newTheme: string): void;
  sidebarCollapsed: boolean;
  setSidebarCollapsed(val: boolean): void;
}

interface Props {
  initialTheme: string;
  children: ReactNode;
}

export const LayoutContext = createContext<LayoutContextType | undefined>(
  undefined,
);

export const LayoutProvider = ({ initialTheme, children }: Props) => {
  const { changeTheme } = useContext(PrimeReactContext);
  const [theme, setTheme] = useState<string>(initialTheme);
  const [themeChanged, setThemeChanged] = useState<boolean>(false);
  const [sidebarCollapsed, setSidebarCollapsed] = useState<boolean>(false);

  let ranonce = false;
  useEffect(() => {
    if (!ranonce) {
      const localTheme = localStorage.getItem('theme');
      if (localTheme) {
        handleChangeTheme(localTheme);
        setTheme(localTheme);
      } else {
        handleChangeTheme(initialTheme);
        setTheme(initialTheme);
      }
      ranonce = true;
    }
  }, []);

  const handleChangeTheme = (newTheme: string) => {
    if (!changeTheme) {
      throw new Error('ChangeTheme context not initialized yet.');
    }
    changeTheme(theme, newTheme, 'theme-link', () => {
      setTheme(newTheme);
      localStorage.setItem('theme', newTheme);
      setThemeChanged(true); // Set themeChanged to true after theme change
    });
  };

  const value = {
    theme,
    handleChangeTheme,
    sidebarCollapsed,
    setSidebarCollapsed,
  };

  return (
    <LayoutContext.Provider value={value}>
      {themeChanged ? children : null}
    </LayoutContext.Provider>
  );
};
