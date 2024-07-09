import React from "react";
import { Task } from "gantt-task-react";
import styles from "./tooltip.module.css";
import { formatTaskStatus, formatStatusDropDown } from "../../utilities/converters";
import { Trans, t } from "@lingui/macro";

const CustomTooltipContent = ({ task, fontSize, fontFamily }) => {
  const style = {
    fontSize,
    fontFamily,
  };

  const getDurationInDays = (start, end) => {
    const durationInMilliseconds = end.getTime() - start.getTime();
    return Math.ceil(durationInMilliseconds / (1000 * 60 * 60 * 24));
  };

  const taskStatus = task.rawTask.status ? formatTaskStatus(task.rawTask.status) : formatStatusDropDown(task.rawTask.projectState);

  return (
    <div className={styles.tooltipDefaultContainer} style={style}>
      <b style={{ fontSize: `${parseInt(fontSize) + 6}px` }}>{task.name}</b>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b><Trans>Status</Trans>:</b> {taskStatus}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b><Trans>Start Date</Trans>:</b> {task.start.toLocaleDateString()}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b><Trans>End Date</Trans>:</b> {task.end.toLocaleDateString()}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b><Trans>Duration</Trans>:</b> {getDurationInDays(task.start, task.end)} <Trans>day(s)</Trans>
      </p>
    </div>
  );
};

export default CustomTooltipContent;
