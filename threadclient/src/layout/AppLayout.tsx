import { Outlet } from 'react-router';
import { useLayout } from '../hooks/useLayout';
import AppSidebar from './AppSidebar';
import AppTopbar from './AppTopbar';

const AppLayout = () => {
  const { sidebarCollapsed } = useLayout();

  return (
    <div className="flex">
      <AppSidebar></AppSidebar>
      <div
        className="surface-ground w-full layout-main"
        style={{
          marginLeft: sidebarCollapsed ? '80px' : ' 220px',
        }}
      >
        <AppTopbar></AppTopbar>
        <div className="mt-5 ml-3 mr-3 ">
          <Outlet></Outlet>
        </div>
      </div>
    </div>
  );
};

export default AppLayout;
