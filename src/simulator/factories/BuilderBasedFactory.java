package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;



/**
 * @author Sergio Villarroel Fernández
 * 
 * A factory for creating BuilderBased objects.
 *
 * @param <T> the generic type
 */
public class BuilderBasedFactory<T> implements Factory<T>{
	
	/** The list. */
	protected ArrayList<Builder<T>> list;

	
	/**
	 * Recibe una lista de constructores 
	 *
	 * @param builders the builders
	 */

	public BuilderBasedFactory(ArrayList<Builder<T>> builders){
		// TODO Auto-generated constructor stub
		this.list= new ArrayList<Builder<T>>();
		this.list=builders;
		
		
	}
	

	/**
	 * Ejecuta los constructores uno a uno hasta que encuentre el constructor
	 *
	 * @param JSONOBject
	 */

	public T createInstance(JSONObject object) throws IllegalArgumentException{
		
		
		for(int i=0; i<list.size();i++){
			if(list.get(i).createInstance2(object)!=null){
				return list.get(i).createInstance2(object);
			}
		}
		
		return null;
		
		
	}
	
	/**
	 * Devuelve en una lista las estructuras JSON devueltas por getBuilderInfoo()
	 *
	 * @return JSONOBject
	 * @throws IllegalArgumentException 
	 */

	public List<JSONObject> getInfo() throws IllegalArgumentException{

		ArrayList<JSONObject> listInfo = new ArrayList<JSONObject>();
		int cont = this.list.size();

		for(int i = 0; i<cont; i++){
			if(!list.get(i).getInfo().isEmpty()) {
				listInfo.add(this.list.get(i).getInfo());
			}else throw new IllegalArgumentException("error getInfo");
			

		}

		return listInfo;

	}
}
