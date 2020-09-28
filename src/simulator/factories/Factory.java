package simulator.factories;

import java.util.List;

import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * The Interface Factory.
 *
 * @param <T> the generic type
 */
public interface Factory<T> {
	
	/**
	 * Creates the instance.
	 *
	 * @param info the info
	 * @return the t
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	public T createInstance(JSONObject info) throws IllegalArgumentException;

	/**
	 * Gets the info.
	 *
	 * @return the info
	 * @throws IllegalArgumentException 
	 */
	public List<JSONObject> getInfo() throws IllegalArgumentException;
}
