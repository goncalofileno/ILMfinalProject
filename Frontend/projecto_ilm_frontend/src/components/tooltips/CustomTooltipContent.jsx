import React from "react";
import { Task } from "gantt-task-react";
import styles from "./tooltip.module.css";
import { formatTaskStatus, formatStatusDropDown } from "../../utilities/converters";

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
        <b>Description:</b> {task.rawTask.description}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b>Status:</b> {taskStatus}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b>Start Date:</b> {task.start.toLocaleDateString()}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b>End Date:</b> {task.end.toLocaleDateString()}
      </p>
      <p className={styles.tooltipDefaultContainerParagraph}>
        <b>Duration:</b> {getDurationInDays(task.start, task.end)} day(s)
      </p>
    </div>
  );
};

export default CustomTooltipContent;
