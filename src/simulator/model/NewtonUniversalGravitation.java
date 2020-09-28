package simulator.model;

import java.util.List;

import simulator.misc.Vector;

/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class NewtonUniversalGravitation.
 */
public class NewtonUniversalGravitation implements GravityLaws{


	public static final double  G = 6.67E-11; 

	/**
	 * Instantiates a new newton universal gravitation.
	 */
	public NewtonUniversalGravitation() {
	}

	/**
	 * Apply the laws
	 *
	 * @param bodies
	 */
	@Override
	public void apply(List<Body> bodies) {
		//Fi = Sumatorio Fij
		Vector aux=new Vector(2);
		//lo que hace este metodo es cambiar la aceleracion de los cuerpos
		for(int i=0;i<bodies.size();i++) {
			Vector sumFuerzas = new Vector(2);
			if(bodies.get(i).getMass() == 0.0) { // si la masa del cuerpo es 0, cambiamos la velocidad y la aceleracion a 0
				bodies.get(i).setAcceleration(new Vector(2));
				bodies.get(i).setVelocity(new Vector(2));
			}else{
				Body b1= bodies.get(i);
				for(int j=0;j<bodies.size();j++) {
					Body b2=bodies.get(j);

					if(i!=j) {

						if(b1.getMass()!=0) {
							//Fi = Sumatorio Fij
							//Fij = dij * fij
							aux=direct(b1,b2).scale(fij(b1,b2));
							sumFuerzas=sumFuerzas.plus(aux);
						}
					}
				}
				//Aceleracion = fi*1/masai
				b1.setAcceleration(sumFuerzas.scale(1/b1.getMass()));
			}
		
		}
	}




	private double fij(Body b1,Body b2) {

		double distancia=b2.getPosition().distanceTo(b1.getPosition());
		distancia *= distancia;
	
	double f = G * ( (b1.getMass() * b2.getMass()) / distancia ) ;
	
	return f;
	
	
	}

	/**
	 * Direct.
	 *
	 * @param b1 
	 * @param b2 
	 * @return  vector
	 */
	private Vector direct(Body b1,Body b2) {
		Vector dir = new Vector(2);
		dir=b2.getPosition().minus(b1.getPosition());
		return 	dir.direction();
		

	}
	
public String toString(){
		
		return("Newton's law of universal gravitation (nlug)");
		
	}

}