package common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by giorgiopea on 14/04/17.
 *
 * Extends the Timer class to add some internal state that is useful for the application
 */
public class StatefulTimer extends Timer {

    private boolean hasBeenScheduled;

    /**
     * Proxies the {@link Timer#scheduleAtFixedRate(TimerTask, long, long)} method so that
     * the {@link StatefulTimer#hasBeenScheduled} field is updated.
     * @param task See {@link Timer#scheduleAtFixedRate(TimerTask, long, long)} documentation.
     * @param delay See {@link Timer#scheduleAtFixedRate(TimerTask, long, long)} documentation.
     * @param period See {@link Timer#scheduleAtFixedRate(TimerTask, long, long)} documentation.
     *
     */
    public void scheduleAtFixedRateProxy(TimerTask task, long delay, long period){
        this.hasBeenScheduled = true;
        this.scheduleAtFixedRate(task,delay,period);
    }

    public boolean isHasBeenScheduled() {
        return hasBeenScheduled;
    }
}
