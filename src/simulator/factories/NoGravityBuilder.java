package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class NoGravityBuilder.
 */
public class NoGravityBuilder extends Builder<GravityLaws> {

	static String _id = "ng";
	
	static String _descr = "No gravity law";

	/**
	 * Instantiates a new no gravity builder.
	 */
	public NoGravityBuilder(){

		super(_id, _descr);

	}
	
	/**
	 * Create the instance
	 *
	 * @param JSONOBject
	 */
	@Override
	public String toString(){
			
			return _descr;
		}
	@Override
	public GravityLaws createTheInstance(JSONObject object) throws IllegalArgumentException {
		// TODO Auto-generated method stub

		return new NoGravity();

	}

	/**
	 * Get the info
	 *
	 * @return JSONOBject
	 */
	@Override
	public JSONObject getInfo() {
		String info = "{\"desc\": \"No gravity\",\"type\": \"ng\",\"data\": {}}";
		// TODO Auto-generated method stub
	 return new JSONObject(info);
	}

}
