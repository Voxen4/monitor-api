package pw.keller.api.config;

public abstract class DynamicSchedule{
    /**
     * Delays scheduler
     * @param milliseconds - the time to delay scheduler.
     */
    abstract void delay(Long milliseconds);

    /**
     * Decreases delay period
     * @param milliseconds - the time to decrease delay period.
     */
    abstract void decreaseDelayInterval(Long milliseconds);

    /**
     * Increases delay period
     * @param milliseconds - the time to increase dela period
     */
    abstract void increaseDelayInterval(Long milliseconds);
}
