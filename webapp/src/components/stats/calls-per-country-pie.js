import { Pie } from "@ant-design/charts";

/**
 * https://charts.ant.design/
 */
const CallsPerCountryPie = ({ data }) => {
  const config = {
    appendPadding: 10,
    data: data,
    angleField: "value",
    colorField: "type",
    radius: 0.75,
    label: {
      type: "spider",
      labelHeight: 28,
      content: "{name}\n{percentage}",
    },
    interactions: [{ type: "element-selected" }, { type: "element-active" }],
  };

  return <Pie {...config} />;
};

export default CallsPerCountryPie;
