package common;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a remote method call. This class groups the name of the method to
 * be invoked on the server/client and the parameters to be passed to this
 * method
 * 
 * @author Andrea Sessa
 * @author Giorgio Pea
 * @version 1.0
 */
public class RemoteMethodCall implements Serializable {

	// A field automatically created for serialization purposes
	private static final long serialVersionUID = 1L;
	// The name of the method this class groups with its parameters
	private String methodName;
	// The list of parameters this class groups with the name of the method they
	// are referred to
	private ArrayList<Object> methodParameters;

	/**
	 * Constructs a remote method call from the name of the method to be invoked
	 * on the server/client and from this method's parameters
	 * 
	 * @param methodName
	 *            the name of the method to be invoked on the server
	 * @param methodParameters
	 *            the list of parameters of the method to be invoked remotely on
	 *            the server/client
	 */
	public RemoteMethodCall(String methodName,
			ArrayList<Object> methodParameters) {
		this.methodName = methodName;
		this.methodParameters = methodParameters;
	}

	/**
	 * Constructs a remote method call from the name of the method to be invoked
	 * on the server/client and from an empty list of this method's
	 * parameters(this means that the method does not accept any parameters)
	 * 
	 * @param methodName
	 *            the name of the method to be invoked on the server/client
	 */
	public RemoteMethodCall(String methodName) {
		this.methodName = methodName;
		this.methodParameters = new ArrayList<Object>();
	}

	/**
	 * Gets the name of the method to be invoked on the server/client
	 * 
	 * @return the name of the method to be invoked on the server/client
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets the list of parameters of the method to be invoked on the
	 * server/client
	 * 
	 * @return the list of parameters of the method to be invoked on the
	 *         server/client
	 */
	public ArrayList<Object> getMethodParameters() {
		return methodParameters;
	}

	@Override
	public String toString() {
		return "RemoteMethodCall [methodName=" + methodName
				+ ", methodParameters=" + methodParameters + "]";
	}
}
