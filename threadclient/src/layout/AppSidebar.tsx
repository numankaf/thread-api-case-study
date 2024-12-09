import { GiProcessor } from 'react-icons/gi';
import { MdOutlineDashboard } from 'react-icons/md';
import { SlDocs } from 'react-icons/sl';
import { TbLogs } from 'react-icons/tb';
import { Menu, MenuItem, Sidebar } from 'react-pro-sidebar';
import { Link, useLocation, useNavigate } from 'react-router';
import { useLayout } from '../hooks/useLayout';

const AppSidebar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { sidebarCollapsed } = useLayout();
  return (
    <Sidebar
      className="shadow-md"
      collapsed={sidebarCollapsed}
      collapsedWidth="80px"
      width="220px"
      backgroundColor="var(--surface-card)"
      rootStyles={{
        height: '100vh',
        mixBlendMode: 'normal',
        border: '0',
      }}
    >
      <div
        className="flex items-center text-primary p-6 w-[220px] cursor-pointer"
        onClick={() => navigate('/dashboard')}
      >
        <GiProcessor size={50} />
        {!sidebarCollapsed && (
          <span className="font-bold text-[20px]">THREAD APP</span>
        )}
      </div>
      <Menu
        rootStyles={{
          padding: '5px',
          userSelect: 'none',
        }}
        closeOnClick
        menuItemStyles={{
          button: {
            '&:hover': {
              backgroundColor: 'var(--surface-hover)',
              color: 'var(--primary-color)',
              borderRadius: '10px',
              userSelect: 'none',
            },
            '&.ps-active': {
              color: 'var(--primary-color)',
              borderRadius: '10px',
            },
          },
          subMenuContent: () => ({
            backgroundColor: 'var(--surface-overlay)',
            borderRadius: '10px',
            width: 'auto',
          }),
        }}
      >
        <MenuItem
          icon={<MdOutlineDashboard size={22} />}
          component={<Link to={'/dashboard'} />}
          active={'/dashboard' === location.pathname}
        >
          Dashboard
        </MenuItem>
        <MenuItem
          icon={<TbLogs size={22} />}
          component={<Link to={'/logs'} />}
          active={'/logs' === location.pathname}
        >
          Logs
        </MenuItem>
        <MenuItem
          icon={<SlDocs size={22} />}
          component={<Link to={'/docs'} />}
          active={'/docs' === location.pathname}
        >
          Documentation
        </MenuItem>
      </Menu>
    </Sidebar>
  );
};

export default AppSidebar;
