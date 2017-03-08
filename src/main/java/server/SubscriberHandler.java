package server;

import java.util.Observer;

/**
 * 
 * Represents a general subscriber handler in the logic of the pub/sub pattern.
 * It observes a topic for changes and notifies its associated subscriber. This
 * interface does not define any behavior because is only used for polymorphism
 * purposes.
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public interface SubscriberHandler extends Observer, Runnable {

}
