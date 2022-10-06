import { useState, useEffect } from "react";

import {
  Modal,
  Table,
  Divider,
  Form,
  Input,
  Button,
  Alert,
  Space,
  Spin,
  Select,
  Upload,
  message,
} from "antd";

import { EyeOutlined, UploadOutlined } from "@ant-design/icons";

import { API_BASE_URL, COUNTRIES } from "../../utils/constants";

const { Option } = Select;

function useGetCallCenters(dataVersion) {
  const [callCenters, setCallCenters] = useState([]);
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  const [pagination, setPagination] = useState({
    current: 1, // TODO: add current page to pagination
    pageSize: 10, // TODO: add pageSize to pagination
    total: 10, // TODO: add total to pagination
  });

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
        setCallCenters(data.content);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, [dataVersion]);

  return { callCenters, pagination, error, isLoading };
}

const CallCentersTable = ({ dataVersion, onView }) => {
  const { callCenters, pagination, error, isLoading } =
    useGetCallCenters(dataVersion);

  if (error) {
    return <Alert message={error.message} type="error" showIcon />;
  }

  const columns = [
    {
      title: "Id",
      dataIndex: "id",
    },
    {
      title: "Name",
      dataIndex: "name",
    },
    {
      title: "Country",
      dataIndex: "countryName",
    },
    {
      title: "Action",
      key: "action",
      render: (text, record) => (
        <Space size="middle">
          <Button
            shape="circle"
            icon={<EyeOutlined />}
            onClick={() => onView(record)}
          />
        </Space>
      ),
    },
  ];

  return (
    <Table
      rowKey={(record) => record.id}
      columns={columns}
      dataSource={callCenters}
      loading={isLoading}
      pagination={pagination}
    />
  );
};

function useGetOneCallCenter(id) {
  const [callCenter, setCallCenter] = useState();
  const [error, setError] = useState();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    fetch(`${API_BASE_URL}/call-centers/${id}`, {
      method: "GET",
      headers: { "Accept": "application/json" },
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot fetch call center");
      })
      .then((data) => {
        setCallCenter(data);
      })
      .catch((e) => {
        console.error(e);
        setError(e);
      })
      .finally(() => setIsLoading(false));
  }, [id]);

  return { callCenter, error, isLoading };
}

const CallCenterDetail = ({ callCenterId, onCancel }) => {
  const { callCenter, error, isLoading } = useGetOneCallCenter(callCenterId);

  return (
    <Modal
      title="Call Center detail"
      visible={true}
      footer={null}
      onCancel={onCancel}
    >
      {isLoading && <Spin />}

      {error && <Alert message={error.message} type="error" showIcon />}

      {callCenter && (
        <>
          <p>
            <b>Name:</b> {callCenter.name}
          </p>
          <p>
            <b>Country:</b> {callCenter.countryName}
          </p>
        </>
      )}
    </Modal>
  );
};

const CallCenterPage = () => {
  const [form] = Form.useForm();
  const [saving, setSaving] = useState(false);
  const [saveSuccess, setSaveSuccess] = useState(false);
  const [saveError, setSaveError] = useState();
  const [dataVersion, setDataVersion] = useState(1);
  const [record, setRecord] = useState(null);

  const onFinish = (values) => {
    setSaving(true);
    setSaveError(null);

    fetch(`${API_BASE_URL}/call-centers`, {
      method: "POST",
      headers: { "Content-type": "application/json", "Accept": "application/json" },
      body: JSON.stringify(values),
    })
      .then((response) => {
        if (response.ok) {
          return response.json();
        }

        throw new Error("Cannot save call center");
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
        <b>New Call Center</b>
      </Divider>
      <Form
        form={form}
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 6 }}
        onFinish={onFinish}
      >
        <Form.Item
          name="name"
          label="Name"
          rules={[
            {
              required: true,
              message: "please enter a name",
            },
          ]}
        >
          <Input allowClear />
        </Form.Item>
        <Form.Item
          name="countryName"
          label="Country"
          rules={[
            {
              required: true,
              message: "please select a country",
            },
          ]}
        >
          <Select
            showSearch
            allowClear
            style={{ width: 200 }}
            placeholder="Select a country"
            optionFilterProp="children"
            filterOption={(input, option) =>
              option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
            }
          >
            {COUNTRIES.map((country) => (
              <Option key={country.name} value={country.name}>
                {country.name}
              </Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button htmlType="submit" type="primary" loading={saving}>
            Save
          </Button>
        </Form.Item>
      </Form>

      {saveSuccess && (
        <Alert message="Call Center saved!" type="info" showIcon closable />
      )}

      {saveError && (
        <Alert message={saveError.message} type="error" showIcon closable />
      )}

      <Divider orientation="left">
        <b>List of Call Centers</b>
      </Divider>

      <CallCentersTable
        dataVersion={dataVersion}
        onView={(selectedRecord) => setRecord(selectedRecord)}
      />

      <Space style={{ marginTop: 20, display: "none" }}>
        <Upload
          name="callCentersFile"
          action={API_BASE_URL + "/call-centers/upload"}
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
            Upload file with call centers
          </Button>
        </Upload>
      </Space>

      {record && (
        <CallCenterDetail
          callCenterId={record.id}
          onCancel={() => setRecord(null)}
        />
      )}
    </>
  );
};

export default CallCenterPage;
