import { GoSidebarCollapse, GoSidebarExpand } from 'react-icons/go';
import { MdOutlineDarkMode, MdOutlineLightMode } from 'react-icons/md';
import { useLayout } from '../hooks/useLayout';

const AppTopbar = () => {
  const { sidebarCollapsed, setSidebarCollapsed, theme, handleChangeTheme } =
    useLayout();

  return (
    <div className="h-[40px]">
      <div className="flex justify-end p-2">
        <div className="flex gap-3">
          <button
            className="no-style-button hover:surface-hover rounded-sm"
            onClick={() => setSidebarCollapsed(!sidebarCollapsed)}
          >
            {sidebarCollapsed ? (
              <GoSidebarCollapse size={22} />
            ) : (
              <GoSidebarExpand size={22} />
            )}
          </button>
          <button
            className="no-style-button hover:surface-hover rounded-sm"
            onClick={() =>
              handleChangeTheme(theme === 'light' ? 'dark' : 'light')
            }
          >
            {theme === 'light' ? (
              <MdOutlineLightMode size={22} />
            ) : (
              <MdOutlineDarkMode size={22} />
            )}
          </button>
        </div>
      </div>
    </div>
  );
};

export default AppTopbar;
