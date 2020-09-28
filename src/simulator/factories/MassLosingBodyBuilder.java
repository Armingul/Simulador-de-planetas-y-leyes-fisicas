package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLossingBody;

/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class MassLosingBodyBuilder.
 */
public class MassLosingBodyBuilder extends Builder <Body>{
	
	static String _id = "mlb";
	
	static String _descr = "Mass losing body";
	

	/**
	 * Instantiates a new mass losing body builder.
	 */
	public MassLosingBodyBuilder() {
		super(_id, _descr);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Create the instance
	 *
	 * @param JSONOBject
	 */
	public Body createTheInstance(JSONObject object) throws IllegalArgumentException{
		Vector vel,acc,pos;
		double[] vell = null,  poss=null, accc=null;
		
			try{
				
				
				JSONArray arrayVel = object.getJSONArray("vel");
				JSONArray arrayPos = object.getJSONArray("pos");
				
				
				vell= new double[arrayVel.length()];
				poss = new double[arrayPos.length()];
				
				vell=super.jsonArrayTodoubleArray(arrayVel);
				poss=super.jsonArrayTodoubleArray(arrayPos);
				accc = new double[2];
				accc[0]=0.0; accc[1]=0.0;
				
			
				
				vel=new Vector(vell);
				pos= new Vector(poss);
				acc=new Vector(accc);
				
				return new MassLossingBody(object.getString("id"), object.getDouble("mass"), vel, acc, pos, object.getDouble("factor"), object.getDouble("freq"));

				
				
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
		String jsonString = "{\"type\": mlb,\"data\": {\"idº\": b1,\"pos\": [-3.5e10, 0.0e00],\"vel\": [0.0e00, 1.4e03],\"mass\": 3.0e28,\"freq\": 1e3,\"factor\": 1e-3}}";
		JSONObject jsonFromString=new JSONObject(jsonString);
		return jsonFromString;
	}
	

	/**
	 * Get the info
	 *
	 * @return JSONOBject
	 */
	public JSONObject getInfo() {
		
		String info = "{\"desc\": \"Body loss mass\",\"type\": mlb";
		JSONObject json = new JSONObject(info);
		json.put("data", createData());
	 return json;
	}
	
}
