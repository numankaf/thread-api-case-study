import { Outlet } from 'react-router';
import AppSidebar from './AppSidebar';
import AppTopbar from './AppTopbar';

const AppLayout = () => {
  return (
    <div className="flex h-full w-full">
      <AppSidebar></AppSidebar>
      <main className="surface-ground w-full">
        <AppTopbar></AppTopbar>
        <div className="mt-5 ml-3 mr-3">
          <Outlet></Outlet>
        </div>
      </main>
    </div>
  );
};

export default AppLayout;
