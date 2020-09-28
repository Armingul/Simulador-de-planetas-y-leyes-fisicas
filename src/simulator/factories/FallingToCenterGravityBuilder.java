package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class FallingToCenterGravityBuilder.
 */
public class FallingToCenterGravityBuilder extends Builder<GravityLaws> {
	
	static String _id = "ftcg";

	static String _descr = "Falling to center gravity";

	/**
	 * Instantiates a new falling to center gravity builder.
	 */
	public FallingToCenterGravityBuilder() {
		super(_id, _descr);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 * Create the instance
	 *
	 * @param JSONOBject
	 */
	@Override
	public GravityLaws createTheInstance(JSONObject object) throws IllegalArgumentException {
			return new FallingToCenterGravity();
		

	}

	/**
	 * Get the info
	 *
	 * @return JSONOBject
	 */
	public JSONObject getInfo() {
		String info = "{\"desc\": \"Falling to center gravity\",\"type\": \"ftcg\",\"data\": {}}";
		return new JSONObject(info);
	}

@Override
public String toString(){
		
		return _descr;
	}


	public static void set_descr(String _descr) {
		FallingToCenterGravityBuilder._descr = _descr;
	}

}
