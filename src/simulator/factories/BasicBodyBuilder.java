package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;



/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class BasicBodyBuilder.
 */
public class BasicBodyBuilder extends Builder<Body> {

	static String _id = "basic";

	static String _descr = "Basic body";

	/**
	 * Instantiates a new basic body builder.
	 */
	public BasicBodyBuilder() {
		super(_id, _descr);
		// TODO Auto-generated constructor stub
	}

	public Body createTheInstance(JSONObject object) throws IllegalArgumentException{

		Vector vel,acc,pos;
		double[] vell = null,  poss=null, accc=null;

		try{

			JSONArray arrayVel = object.getJSONArray("vel");
			JSONArray arrayPos = object.getJSONArray("pos");

			vell=super.jsonArrayTodoubleArray(arrayVel);
			poss=super.jsonArrayTodoubleArray(arrayPos);
			accc = new double[2];
			accc[0]=0.0; accc[1]=0.0;

			vel=new Vector(vell);
			pos= new Vector(poss);
			acc=new Vector(accc);
			return new Body(object.getString("id"), object.getDouble("mass"), vel, acc, pos);

		}catch(Exception e){
			throw new IllegalArgumentException("Error to create instance NewtonUniversalGravit");
		}

	}

	/**
	 * Creates the data.
	 *
	 * @return the JSON object
	 */
	protected JSONObject createData(){
		String jsonString = "{\"type\": basic,\"data\": {\"id\": b1,\"pos\": [0.0e00, 0.0e00],\"vel\": [0.05e04, 0.0e00],\"mass\": 5.97e24}}";
		JSONObject joFromString = new JSONObject(jsonString);
		return joFromString;

	}


	public JSONObject getInfo() {

		String info = "{\"desc\": \"Basic body\",\"type\": basic";
		JSONObject json = new JSONObject(info);
		json.put("data", createData());
		return json;
	}

}
