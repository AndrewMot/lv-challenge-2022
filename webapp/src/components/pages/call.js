import { useState, useEffect } from "react";

import { Table, Divider, Alert, Button, Space, Upload, message } from "antd";

import { UploadOutlined } from "@ant-design/icons";

import { API_BASE_URL } from "../../utils/constants";

function useGetCalls() {
  const [calls, setCalls] = useState([]);
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  const [pagination, setPagination] = useState({
    current: 1, // TODO: add current page to pagination
    pageSize: 10, // TODO: add pageSize to pagination
    total: 10, // TODO: add total to pagination
  });

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/calls`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch calls");
      })
      .then((data) => {
        setCalls(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, []);

  return { calls, pagination, error, isLoading };
}

const CallsTable = () => {
  const { calls, pagination, error, isLoading } = useGetCalls();

  if (error) {
    return <Alert message={error.message} type="error" showIcon />;
  }

  const columns = [
    {
      title: "Id",
      dataIndex: "id",
    },
    {
      title: "Customer",
      dataIndex: "customerFullName",
    },
    {
      title: "Customer phone",
      dataIndex: "customerPhone",
    },
    {
      title: "Customer country phone code",
      dataIndex: "customerCountryPhoneCode",
    },
    {
      title: "Agent extension",
      dataIndex: "agentExtension",
    },
    {
      title: "Received on",
      dataIndex: "receivedOn",
    },
  ];

  return (
    <Table
      rowKey="id"
      columns={columns}
      dataSource={calls}
      pagination={pagination}
      loading={isLoading}
    />
  );
};

const CallPage = () => (
  <>
    <Divider orientation="left">
      <b>Received Calls</b>
    </Divider>

    <CallsTable />

    <Space style={{ marginTop: 20, display: "none" }}>
      <Upload
        name="callsFile"
        action={API_BASE_URL + "/calls/upload"}
        showUploadList={{ showRemoveIcon: false }}
        onChange={(info) => {
          if (info.file.status === "done") {
            message.success(`${info.file.name} file uploaded successfully`);
          }

          if (info.file.status === "error") {
            message.error(`${info.file.name} file upload failed.`);
          }
        }}
      >
        <Button type="primary" icon={<UploadOutlined />}>
          Upload file with calls
        </Button>
      </Upload>
    </Space>
  </>
);

export default CallPage;
