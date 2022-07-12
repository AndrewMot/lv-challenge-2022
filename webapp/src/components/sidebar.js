import React from "react";

import { Link, useLocation } from "react-router-dom";

import {
  DashboardOutlined,
  PhoneOutlined,
  UserOutlined,
  ApartmentOutlined,
} from "@ant-design/icons";

import { Layout, Menu } from "antd";

const MENU_ITEMS = [
  { url: "/", title: "Dashboard", icon: <DashboardOutlined /> },
  { url: "/calls", title: "Calls", icon: <PhoneOutlined /> },
  { url: "/agents", title: "Agents", icon: <UserOutlined /> },
  { url: "/call-centers", title: "Call Centers", icon: <ApartmentOutlined /> },
];

const SideBar = () => {
  const { pathname } = useLocation();

  const activeMenuItem = MENU_ITEMS.find(
    (menuItem) => menuItem.url === pathname
  );

  return (
    <Layout.Sider breakpoint="lg" collapsedWidth="0">
      <div className="app-logo">Livevox Challenge</div>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={[activeMenuItem.title]}
      >
        {MENU_ITEMS.map((menuItem) => (
          <Menu.Item key={menuItem.title} icon={menuItem.icon}>
            <Link to={menuItem.url}>{menuItem.title}</Link>
          </Menu.Item>
        ))}
      </Menu>
    </Layout.Sider>
  );
};

export default SideBar;
