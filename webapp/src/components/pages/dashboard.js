import React from "react";
import { Statistic, Card, Row, Col, Divider } from "antd";
import { ShareAltOutlined, UserSwitchOutlined, PhoneOutlined, WarningOutlined } from "@ant-design/icons";

import CallsPerCountryPie from "../stats/calls-per-country-pie";

const callsPerCountryData = [
  {
    type: "Colombia",
    value: 27,
  },
  {
    type: "India",
    value: 25,
  },
  {
    type: "United States",
    value: 18,
  },
  {
    type: "Australia",
    value: 15,
  },
];

const Dashboard = () => {
  return (
    <div className="site-statistic-demo-card">
      <Divider orientation="left">
        <b>Dashboard</b>
      </Divider>
      <Row gutter={16} style={{ marginBottom: '10px' }}>
        <Col span={12}>
          <Card>
            <Statistic
              title="Routed agents"
              value={0}
              precision={0}
              valueStyle={{ color: "#3f8600" }}
              prefix={<UserSwitchOutlined />}
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card>
            <Statistic
              title="Routed calls"
              value={0}
              precision={0}
              valueStyle={{ color: "#3f8600" }}
              prefix={<ShareAltOutlined />}
            />
          </Card>
        </Col>
      </Row>
      <Row gutter={16} >
        <Col span={12}>
          <Card>
            <Statistic
              title="Processed calls"
              value={0}
              precision={0}
              valueStyle={{ color: "#3f8600" }}
              prefix={<PhoneOutlined />}
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card>
            <Statistic
              title="Missed calls"
              value={0}
              precision={0}
              valueStyle={{ color: "#cf1322" }}
              prefix={<WarningOutlined />}
            />
          </Card>
        </Col>
      </Row>
      <Divider orientation="left">
        <b>Calls per country</b>
      </Divider>
      <Row gutter={16}>
        <Col span={20}>
          <CallsPerCountryPie data={callsPerCountryData} />
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
