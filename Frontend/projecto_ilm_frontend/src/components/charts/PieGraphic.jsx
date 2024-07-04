import React, { PureComponent } from "react";
import { PieChart, Pie, Sector, Cell, ResponsiveContainer } from "recharts";

export default class PieGraphic extends PureComponent {
  render() {
    const { data, colors } = this.props;

    const RADIAN = Math.PI / 180;
    const renderCustomizedLabel = ({
      cx,
      cy,
      midAngle,
      innerRadius,
      outerRadius,
      percent,
      index,
    }) => {
      const radius = 15 + innerRadius + (outerRadius - innerRadius);
      const x = cx + radius * Math.cos(-midAngle * RADIAN);
      const y = cy + radius * Math.sin(-midAngle * RADIAN);

      const radius2 = innerRadius + (outerRadius - innerRadius) * 0.5;
      const x2 = cx + radius2 * Math.cos(-midAngle * RADIAN);
      const y2 = cy + radius2 * Math.sin(-midAngle * RADIAN);

      return (
        <>
          {data[index].value !== 0 && (
            <>
              <text
                x={x2}
                y={y2}
                fill="white"
                style={{ fontSize: "19px" }}
                textAnchor={x2 > cx ? "start" : "end"}
                dominantBaseline="central"
              >
                {`${data[index].value}`}
              </text>
              <text
                x={x}
                y={y}
                textAnchor={x > cx ? "start" : "end"}
                dominantBaseline="central"
                style={{ fill: colors[index], fontWeight: "bold" }}
              >
                {`${data[index].name}`}
              </text>
            </>
          )}
        </>
      );
    };

    return (
      <PieChart width={400} height={400}>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          labelLine={false}
          label={renderCustomizedLabel}
          outerRadius={80}
          fill="#8884d8"
          dataKey="value"
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
          ))}
        </Pie>
      </PieChart>
    );
  }
}
