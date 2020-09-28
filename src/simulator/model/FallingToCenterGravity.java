package simulator.model;
import java.util.List;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class FallingToCenterGravity.
 */
public class FallingToCenterGravity implements GravityLaws{

	/** The g. */
	private final double g =-9.81;

	/**
	 * Instantiates a new falling to center gravity.
	 */
	public FallingToCenterGravity() {

	}

	/**
	 * Apply the laws
	 *
	 * @param bodies
	 */
	public void apply(List<Body> bodies) {

		for(Body b:bodies) {
			b.setAcceleration(b.getPosition().direction().scale(g)); 

		}

	}
	
	public String toString(){
		
		return("Falling to center gravity (ftcg)");
		
	}

}
