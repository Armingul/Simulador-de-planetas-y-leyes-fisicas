package simulator.factories;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class Builder.
 *
 * @param <T> the generic type
 */
public abstract class Builder <T>{
	 

 	protected String _id;
 	protected String _desc;
	
	/**
	 * Instantiates a new builder.
	 *
	 * @param id the id
	 * @param descr the descr
	 */
	public Builder(String id, String descr) {
		this._id=id;
		this._desc=descr;
	}
	
	/**
	 * Json array todouble array.
	 *
	 * @param array the array
	 * @return the double[]
	 */
	protected double[] jsonArrayTodoubleArray(JSONArray array){
		double[] doubleArr =new double[array.length()];
		
		for(int i= 0; i<array.length();i++){
			doubleArr[i]=array.getDouble(i);
		}
		return doubleArr;
	}
	
	/**
	 * Creates the instance 2.
	 *
	 * @param info the info
	 * @return the t
	 * @throws JSONException the JSON exception
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	@SuppressWarnings("unchecked")
	public T createInstance2(JSONObject info) throws IllegalArgumentException{
		Object b = null;
		if (_id != null && _id.equals(info.getString("type"))){

			b = createTheInstance(info.getJSONObject("data"));
			return (T) b;

		}else return null;


	}

	/**
	 * Gets the builder info 2.
	 *
	 * @return the builder info 2
	 */
	public JSONObject getBuilderInfo2() {
		JSONObject info = new JSONObject();
		info.put("type", _id);
		info.put("data", createData2());
		info.put("desc", _desc);
		return info;
		}
	
	/**
	 * Creates the data 2.
	 *
	 * @return the JSON object
	 */
	protected JSONObject createData2() {
		return new JSONObject();
	}

	

	/**
	 * Creates the the instance.
	 *
	 * @param object the object
	 * @return the object
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	abstract  Object createTheInstance(JSONObject object) throws IllegalArgumentException;
	
	/**
	 * Gets the info.
	 *
	 * @return the info
	 */
	abstract  JSONObject getInfo();

	

	
}
