package org.fcesur.skillengine.rummy.game;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;

import java.util.Date;
import java.util.TimerTask;

import static java.util.Objects.nonNull;

@Slf4j
public abstract class GameTimer {

    private Date startTime;
    private Date endTime;

    public void start(@Nullable Date time) {

        startTime = nonNull(time)
              ? time
              : new Date(System.currentTimeMillis());

        long l = startTime.getTime() - System.currentTimeMillis();

        if (l > 0) {

            TimerTask task = new TimerTask() {

                public void run() {
                    try {
                        startCallback();
                    } catch (Exception ex) {
                        log.error("Failed to start callback", ex);
                    }
                }
            };

        } else {
            startCallback();
        }
    }

    public void end(Date endTime) {
        if (endTime == null) {
            this.endTime = new Date(System.currentTimeMillis());
        } else {
            this.endTime = endTime;
        }
        validate();
    }

    public Date getEndTime() {
        validate();
        return endTime;
    }

    public Date getStartTime() {
        validate();
        return startTime;
    }

    public boolean hasEnded() {
        validate();
        Date now = new Date(System.currentTimeMillis());
        if (endTime != null && (endTime.compareTo(now) <= 0)) {
            return true;
        }
        return false;
    }

    public boolean hasStarted() {
        validate();
        if (startTime != null) {
            Date now = new Date(System.currentTimeMillis());
            if (startTime.compareTo(now) <= 0) {
                return true;
            }
        }
        return false;
    }

    private void validate() {
        boolean isValid = true;
        if (startTime != null) {
            if (endTime != null) {
                if (startTime.compareTo(endTime) > 0) {
                    isValid = false;
                }
            }
        } else {
            if (endTime != null) {
                isValid = false;
            }
        }
    }

    public abstract void startCallback();
}
