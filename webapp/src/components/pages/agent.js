import { useState, useEffect } from "react";

import {
  Modal,
  Table,
  Divider,
  Form,
  Select,
  Input,
  Checkbox,
  Button,
  Alert,
  Tooltip,
  Space,
  Spin,
  Upload,
  message,
} from "antd";

import {
  EyeOutlined,
  UserSwitchOutlined,
  UploadOutlined,
} from "@ant-design/icons";

import { API_BASE_URL } from "../../utils/constants";

const { Option } = Select;

function useGetAgents(dataVersion) {
  const [agents, setAgents] = useState([]);
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  const [pagination, setPagination] = useState({
    current: 1, // TODO: add current page to pagination
    pageSize: 10, // TODO: add pageSize to pagination
    total: 10, // TODO: add total to pagination
  });

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/agents`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch agents");
      })
      .then((data) => {
        setAgents(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, [dataVersion]);

  return { agents, pagination, error, isLoading };
}

const AgentsTable = ({ dataVersion, onView, onAssign }) => {
  const { agents, pagination, error, isLoading } = useGetAgents(dataVersion);

  if (error) {
    return <Alert message={error.message} type="error" showIcon />;
  }

  const columns = [
    {
      title: "Id",
      dataIndex: "id",
    },
    {
      title: "First Name",
      dataIndex: "firstName",
    },
    {
      title: "Last Name",
      dataIndex: "lastName",
    },
    {
      title: "Extension",
      dataIndex: "extension",
    },
    {
      title: "Active?",
      dataIndex: "active",
      render: (value, record) => <>{value === true ? "Yes" : "No"}</>,
    },
    {
      title: "Call Center",
      dataIndex: "callCenterName",
      render: (value, record) => (
        <>{record.callCenterId ? record.callCenterName : "None"}</>
      ),
    },
    {
      title: "Action",
      key: "action",
      render: (text, record) => (
        <Space size="middle">
          <Tooltip placement="bottom" title="Details">
            <Button
              shape="circle"
              icon={<EyeOutlined />}
              onClick={() => onView(record)}
            />
          </Tooltip>
          <Tooltip placement="bottom" title="Assign to call center">
            <Button
              shape="circle"
              icon={<UserSwitchOutlined />}
              onClick={() => onAssign(record)}
            />
          </Tooltip>
        </Space>
      ),
    },
  ];

  return (
    <Table
      rowKey={(record) => record.id}
      columns={columns}
      dataSource={agents}
      pagination={pagination}
      loading={isLoading}
    />
  );
};

function useGetOneAgent(id) {
  const [agent, setAgent] = useState();
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/agents/${id}`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch agent");
      })
      .then((data) => {
        setAgent(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, [id]);

  return { agent, error, isLoading };
}

const AgentDetail = ({ agentId, onCancel }) => {
  const { agent, error, isLoading } = useGetOneAgent(agentId);

  return (
    <Modal
      title="Agent detail"
      visible={true}
      footer={null}
      onCancel={onCancel}
    >
      {isLoading && <Spin />}

      {error && <Alert message={error.message} type="error" showIcon />}

      {agent && (
        <>
          <p>
            <b>Full Name:</b> {agent.firstName + " " + agent.lastName}
          </p>
          <p>
            <b>Extension:</b> {agent.extension}
          </p>
          <p>
            <b>Active:</b> {agent.active ? "Yes" : "No"}
          </p>
        </>
      )}
    </Modal>
  );
};

function useGetCallCenters() {
  const [callCenters, setCallCenters] = useState([]);
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/call-centers`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch call centers");
      })
      .then((data) => {
        setCallCenters(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, []);

  return { callCenters, error, isLoading };
}

const CallCenterSelect = () => {
  const { callCenters, error, isLoading } = useGetCallCenters([]);

  return (
    <Form.Item name="callCenterId" noStyle>
      <Select
        showSearch
        allowClear
        style={{ width: 300 }}
        placeholder="Select a call center"
        optionFilterProp="children"
        filterOption={(input, option) =>
          option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
        }
        loading={isLoading}
        help={error && error.message}
      >
        <Option key="0" value={null}>
          None
        </Option>

        {callCenters.map((callCenter) => (
          <Option key={callCenter.id} value={callCenter.id}>
            {callCenter.name}
          </Option>
        ))}
      </Select>
    </Form.Item>
  );
};

const AgentAssignment = ({ agentId, onCancel, onSave }) => {
  const { agent, error, isLoading } = useGetOneAgent(agentId);
  const [saving, setSaving] = useState(false);
  const [saveError, setSaveError] = useState();
  const [form] = Form.useForm();

  const anyError = error ?? saveError;

  const onFinish = (values) => {
    setSaving(true);
    setSaveError(null);

    fetch(`${API_BASE_URL}/agents/${agentId}/assign`, {
      method: "PATCH",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify({
        ...values,
        callCenterId: values.callCenterId ?? null,
      }),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot save agent assignment");
      })
      .then(() => {
        form.resetFields();
        onSave();
      })
      .catch((err) => setSaveError(err))
      .finally(() => setSaving(false));
  };

  return (
    <Modal
      title="Assign agent to call center"
      visible={true}
      footer={null}
      onCancel={onCancel}
    >
      {isLoading && <Spin />}

      {anyError && (
        <Alert message={anyError.message} type="error" showIcon closable />
      )}

      {agent && (
        <Form
          form={form}
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 6 }}
          initialValues={{
            agentId: agent.id,
            callCenterId: agent.callCenterId ?? null,
          }}
          onFinish={onFinish}
        >
          <Form.Item label="Agent">
            {agent.firstName} {agent.lastName}
          </Form.Item>
          <Form.Item name="agentId" label="Agent" hidden>
            <Input />
          </Form.Item>
          <Form.Item label="Call Center">
            <CallCenterSelect />
          </Form.Item>
          <Form.Item>
            <Button htmlType="submit" type="primary" loading={saving}>
              Save
            </Button>
          </Form.Item>
        </Form>
      )}
    </Modal>
  );
};

const AgentPage = () => {
  const [form] = Form.useForm();
  const [saving, setSaving] = useState(false);
  const [saveSuccess, setSaveSuccess] = useState(false);
  const [saveError, setSaveError] = useState();
  const [dataVersion, setDataVersion] = useState(1);
  const [viewRecord, setViewRecord] = useState();
  const [assignRecord, setAssignRecord] = useState();

  const onFinish = (values) => {
    setSaving(true);
    setSaveError(null);

    fetch(`${API_BASE_URL}/agents`, {
      method: "POST",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify(values),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot save agent");
      })
      .then(() => {
        form.resetFields();
        setSaveSuccess(true);
        setDataVersion(dataVersion + 1);
      })
      .catch((err) => setSaveError(err))
      .finally(() => setSaving(false));
  };

  useEffect(() => {
    if (saveSuccess) {
      setTimeout(() => setSaveSuccess(false), 5 * 1000);
    }
  }, [saveSuccess]);

  return (
    <>
      <Divider orientation="left">
        <b>New Agent</b>
      </Divider>
      <Form
        form={form}
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 6 }}
        onFinish={onFinish}
        initialValues={{
          callCenterId: null,
        }}
      >
        <Form.Item
          name="firstName"
          label="First Name"
          rules={[
            {
              required: true,
              message: "please enter first name",
            },
          ]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item
          name="lastName"
          label="Last Name"
          rules={[
            {
              required: true,
              message: "please enter last name",
            },
          ]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item
          name="extension"
          label="Extension"
          wrapperCol={{ span: 2 }}
          rules={[
            {
              required: true,
              pattern: /^[0-9]{3}$/,
              message: "please enter a 3-digit number",
            },
          ]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item label="Call Center">
          <CallCenterSelect />
        </Form.Item>
        <Form.Item name="active" valuePropName="checked" label="Active">
          <Checkbox />
        </Form.Item>
        <Form.Item>
          <Button htmlType="submit" type="primary" loading={saving}>
            Save
          </Button>
        </Form.Item>
      </Form>

      {saveSuccess && (
        <Alert message="Agent saved!" type="info" showIcon closable />
      )}

      {saveError && (
        <Alert message={saveError.message} type="error" showIcon closable />
      )}

      <Divider orientation="left">
        <b>List of Agents</b>
      </Divider>

      <AgentsTable
        dataVersion={dataVersion}
        onView={(selectedRecord) => setViewRecord(selectedRecord)}
        onAssign={(selectedRecord) => setAssignRecord(selectedRecord)}
      />

      <Space style={{ marginTop: 20, display: "none" }}>
        <Upload
          name="agentsFile"
          action={API_BASE_URL + "/agents/upload"}
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
            Upload file with agents
          </Button>
        </Upload>
      </Space>

      {viewRecord && (
        <AgentDetail
          agentId={viewRecord.id}
          onCancel={() => setViewRecord(null)}
        />
      )}

      {assignRecord && (
        <AgentAssignment
          agentId={assignRecord.id}
          onCancel={() => setAssignRecord(null)}
          onSave={() => {
            setAssignRecord(null);
            setDataVersion(dataVersion + 1);
          }}
        />
      )}
    </>
  );
};

export default AgentPage;
