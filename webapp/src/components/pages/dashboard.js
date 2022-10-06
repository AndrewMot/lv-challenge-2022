import { React, useState, useEffect } from "react";
import { Statistic, Card, Row, Col, Divider, Alert } from "antd";
import { ShareAltOutlined, UserSwitchOutlined, PhoneOutlined, WarningOutlined } from "@ant-design/icons";

import { API_BASE_URL, COUNTRIES } from "../../utils/constants";

import CallsPerCountryPie from "../stats/calls-per-country-pie";

function useGetCallsPerCountry() {
  const [error, setError] = useState();
  const [callsPerCountryData, setCallsPerCountryData] = useState([]);

  useEffect(() => {
    fetch(`${API_BASE_URL}/calls/country`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch calls per country");
      })
      .then((data) => {
        const result = [];
        for(var c in data) {
          result.push({
            type: COUNTRIES.find((country) => country.phoneCode === data[c].countryCode).name,
            value: data[c].count
          });
        }
        setCallsPerCountryData(result);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      });
  }, []);

  return { callsPerCountryData, error };
}

function useGetRoutedCalls() {
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);
  const [routedCalls, setRoutedCalls] = useState();

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/calls/routed`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch routed calls");
      })
      .then((data) => {
        setRoutedCalls(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, []);

  return { routedCalls, error, isLoading };
}

const CountryPie = () => {
  const { callsPerCountryData, error } = useGetCallsPerCountry();
  if (error) {
    return <Alert message={error.message} type="error" showIcon />;
  }
  return <CallsPerCountryPie data={callsPerCountryData} />;
};

const Dashboard = () => {
  const { routedCalls, error, isLoading } = useGetRoutedCalls();
  if (error) {
    return <Alert message={error.message} type="error" showIcon />;
  }
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
              value={routedCalls}
              precision={0}
              valueStyle={{ color: "#3f8600" }}
              prefix={<ShareAltOutlined />}
              loading={isLoading}
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
          <CountryPie />
        </Col>
      </Row>
    </div>
  );
};

export default Dashboard;
