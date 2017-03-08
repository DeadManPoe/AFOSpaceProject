package common;

import java.io.Serializable;

/**
 * Represents a generic notification the server sends to the client in response
 * to one of its requests. A base, generic notification contains a text message
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class ClientNotification implements Serializable {
	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	// The text message of the notification
	private String message;

	/**
	 * Constructs a generic notification the server sends to the client in
	 * response to one of its requests. This notification is constructed from a
	 * text message
	 * 
	 * @param message
	 *            the text message contained by the notification
	 */
	public ClientNotification(String message) {
		this.message = message;
	}

	/**
	 * Constructs a generic notification the server sends to the client in
	 * response to one of its requests. This notification is constructed from a
	 * text message that is empty
	 */
	public ClientNotification() {
		this("");
	}

	/**
	 * Gets the text message contained in the notification
	 * 
	 * @return the text message contained in the notification
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the text message contained in the notification
	 * 
	 * @param message
	 *            the new text message to be contained in the notification
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
