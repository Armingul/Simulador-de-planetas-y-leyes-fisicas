package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;



/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class NewtonUniversalGravitationBuilder.
 */
public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws> {

	static String _id = "nlug";
	
	static String _descr = "Newton universal law";



	/**
	 * Instantiates a new newton universal gravitation builder.
	 */
	public NewtonUniversalGravitationBuilder() {
		super(_id, _descr);
		// TODO Auto-generated constructor stub
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
	public GravityLaws createTheInstance(JSONObject object) throws IllegalArgumentException{


		return new NewtonUniversalGravitation();

	}

	/**
	 * Get the info
	 *
	 * @return JSONOBject
	 */
	public JSONObject getInfo() 
	{
		String info = "{\"desc\": \"Newton universal gravitation\",\"type\": \"nlug\",\"data\": {}}";
		// TODO Auto-generated method stub
		return new JSONObject(info);
	}
}
