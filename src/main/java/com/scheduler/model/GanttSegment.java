package com.scheduler.model;

/**
 * Represents a segment in the Gantt chart timeline.
 */
public class GanttSegment {
    private String pid;
    private int start;
    private int end;

    public GanttSegment(String pid, int start, int end) {
        this.pid = pid;
        this.start = start;
        this.end = end;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getDuration() {
        return end - start;
    }

    public boolean isIdle() {
        return "idle".equals(pid);
    }

    @Override
    public String toString() {
        return pid + ":" + start + "–" + end;
    }
}
