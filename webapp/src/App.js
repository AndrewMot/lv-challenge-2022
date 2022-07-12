import React from "react";
import "./App.css";

import SideBar from "./components/sidebar";
import {
  DashboardPage,
  CallPage,
  AgentPage,
  CallCenterPage,
} from "./components/pages";

import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

import { Layout } from "antd";
const { Content } = Layout;

const App = () => {
  return (
    <Router>
      <Layout className="app-layout">
        <SideBar />
        <Layout>
          <Content style={{ margin: "24px 16px 0" }}>
            <div
              className="app-layout-background"
              style={{ padding: 24, minHeight: 360 }}
            >
              <Switch>
                <Route path="/" exact component={DashboardPage} />
                <Route path="/agents" component={AgentPage} />
                <Route path="/call-centers" component={CallCenterPage} />
                <Route path="/calls" component={CallPage} />
              </Switch>
            </div>
          </Content>
        </Layout>
      </Layout>
    </Router>
  );
};

export default App;
