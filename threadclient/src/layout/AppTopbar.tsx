import { Button } from 'primereact/button';
import { useLayout } from '../hooks/useLayout';

const AppTopbar = () => {
  const { sidebarCollapsed, setSidebarCollapsed, theme, handleChangeTheme } =
    useLayout();

  return (
    <div className="h-[40px]">
      <div className="flex justify-between">
        AppTopbar
        <div>
          <Button
            label="theme"
            onClick={() =>
              handleChangeTheme(theme === 'light' ? 'dark' : 'light')
            }
          ></Button>
          <Button
            label="collapse"
            onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
          ></Button>
        </div>
      </div>
    </div>
  );
};

export default AppTopbar;
