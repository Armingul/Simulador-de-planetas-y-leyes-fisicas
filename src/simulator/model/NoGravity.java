package simulator.model;

import java.util.List;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class NoGravity.
 */
public class NoGravity implements GravityLaws {

	/**
	 * Instantiates a new no gravity.
	 */
	public NoGravity(){

	}

	@Override
	public void apply(List<Body> bodies) {
		// TODO Auto-generated method stub


	}
public String toString(){
		
		return("No Gravity (ng)");
		
	}
}
