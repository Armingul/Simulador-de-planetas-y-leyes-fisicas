package simulator.model;

import java.util.List;


/**
 * @author Sergio Villarroel Fern�ndez
 * 
 * The Interface GravityLaws.
 */
public interface GravityLaws {
	
	/**
	 * Apply.
	 *
	 * @param l 
	 */
	public void apply(List<Body> l);
}
