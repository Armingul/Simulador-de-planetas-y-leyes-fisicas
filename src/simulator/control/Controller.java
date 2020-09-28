package simulator.control;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;





import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class Controller.
 */
public class Controller {

	private PhysicsSimulator simul; //Ejecuta las diferentes operaciones
	private Factory<Body> factory;	//Construye cuerpos que se leen del fichero
	private Factory<GravityLaws> Gfactory;
	private GravityLaws la;
	
	/**
	 * Instantiates a new controller.
	 *
	 * @param sim the sim
	 * @param fact the fact
	 */
	public Controller(PhysicsSimulator sim, Factory<Body> fact, Factory<GravityLaws> GravityFactory){
		this.simul=sim;
		this.factory=fact;
		this.Gfactory = GravityFactory;
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Run.
	 *
	 * @param x the x
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void run(int x) throws IOException{
		OutputStream out = null;
	
		run(x,out);
		
	}
	
	/**
	 * Run.
	 *
	 * @param x the x
	 * @param ti the ti
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void run(int x, OutputStream ti) throws IOException{
		
		String bodies = "\"states\": [";
		System.out.println("{"+"\n"+bodies);
		
		for(int i=0; i<x;i++){
			System.out.println(simul.toString()+",");
			simul.advance();
		}
		System.out.println(simul.toString());
		System.out.println("]");
		System.out.println("}");
	}
	
	/**
	 * Run buffered.
	 *
	 * @param x the x
	 * @param ti the ti
	 * @return the boolean
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Boolean runBuffered(int x, BufferedWriter ti) throws IOException{
		
		String bodies = "\"states\": [";
		
		ti.write("{"+"\n"+bodies);
		ti.newLine();
		
		for(int i=0; i<x;i++){
			
			ti.write(simul.toString()+",");
			ti.newLine();
			simul.advance();
		}
		ti.write(simul.toString());
		ti.newLine();
		ti.write("]");
		ti.newLine();
		ti.write("}");
		

		return true;
	}


	/**
	 * Load bodies.
	 *
	 * @param in the in
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws JSONException 
	 */
	public void loadBodies(InputStream in) throws IllegalArgumentException, JSONException, simulator.factories.IllegalArgumentException{


		JSONObject jsonInput = new JSONObject(new JSONTokener(in));

		if(jsonInput.has("bodies")){

			JSONArray bodies=jsonInput.getJSONArray("bodies");

			for(int i=0; i<bodies.length(); i++){
				simul.addBody(factory.createInstance(bodies.getJSONObject(i)));
			}

		}

	}
	
	/**
	 * Store.
	 *
	 * @param output the output
	 * @param s the s
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean store(BufferedWriter output, String s) throws IOException {
		boolean guardado=false;
		
		try{
			output.write(s);
			output.newLine();
		
		}catch(IOException e){
			System.out.println(e);
		}


		return guardado;
	}

	public void reset(){
		
		this.simul.reset();
	}
	
	public void setDeltaTime(double dt){
		this.simul.setDeltaTime(dt);
	}
	
	public void addObserver(SimulatorObserver o){
		this.simul.addObserver(o);
	}
	
	public Factory<GravityLaws> getGravityLawsFactory(){
		
		return this.Gfactory;
		
	}
	public void setGravityLaws(JSONObject info){
		
		try {
			this.la = this.Gfactory.createInstance(info);
			simul.setGravityLaws(la);
		} catch (simulator.factories.IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public GravityLaws getGravityLaws() {
		return this.la;
	}
}
