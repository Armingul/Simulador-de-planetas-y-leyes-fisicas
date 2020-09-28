package simulator.model;

import simulator.misc.Vector;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class Body.
 */
public class Body {

	private String id;
	
	private Double mass;

	private Vector velocity;

	private Vector acceleration;

	private Vector position;
	
	/**
	 * Instantiates a new body.
	 *
	 * @param id 
	 * @param mas 
	 * @param vel 
	 * @param acc 
	 * @param pos 
	 */
	public Body(String id, Double mas, Vector vel, Vector acc, Vector pos){
		this.id=id;
		this.mass=mas;
		this.velocity=vel;
		this.acceleration=acc;
		this.position=pos;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return  id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gets the velocity.
	 *
	 * @return  velocity
	 */
	public Vector getVelocity() {
		return velocity;
	}
	
	/**
	 * Gets the acceleration.
	 *
	 * @return  acceleration
	 */
	public Vector getAcceleration() {
		return acceleration;
	}
	
	/**
	 * Gets the position.
	 *
	 * @return  position
	 */
	public Vector getPosition() {
		return position;
	}
	
	/**
	 * Gets the mass.
	 *
	 * @return  mass
	 */
	public Double getMass() {
		return mass;
	}
	
	/**
	 * Sets the velocity.
	 *
	 * @param velocity the new velocity
	 */
	protected void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}
	
	/**
	 * Sets the acceleration.
	 *
	 * @param acceleration the new acceleration
	 */
	protected void setAcceleration(Vector acceleration) {
		this.acceleration = acceleration;
	}
	
	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	protected void setPosition(Vector position) {
		this.position = position;
	}
	
	/**
	 * Move.
	 *
	 * @param t 
	 */
	protected void move(double t){
		Vector pos, vel;
		double i = 0.5;
		
		if(t==0 || t<0){
			throw new IllegalArgumentException("Bar argument, time simulation");
		}else{
			pos=getPosition();
			pos=pos.plus(getVelocity().scale(t));
			pos=pos.plus(getAcceleration().scale(t*t).scale(i));
			this.setPosition(pos);
			
			
			vel=getVelocity();
			vel=vel.plus(getAcceleration().scale(t));
			this.setVelocity(vel);
		}
		
		
		
	}
	

	public String toString(){
		char comillas = (char)34;
		return " {  "+comillas+"id"+comillas+": "+comillas+getId()+comillas+ ", "+comillas+ "mass" +comillas+": "+getMass()+","+comillas+ "pos"+comillas+ ": " +getPosition()+","+ comillas +"vel"+comillas+": "+getVelocity()+","+comillas+ "acc"+comillas+": "+getAcceleration()+ " } ";
	}
	
	/**
	 * Equals.
	 *
	 * @param id 
	 * @return true, if successful
	 */
	public boolean equals(String id){
		
		if(id==getId()){
			return true;
		}else return false;
		
		
	}

	/**
	 * Sets the mass.
	 *
	 * @param i the new mass
	 */
	protected void setMass(double i) {
		this.mass=i;
		// TODO Auto-generated method stub
		
	}


	
	
	
}
