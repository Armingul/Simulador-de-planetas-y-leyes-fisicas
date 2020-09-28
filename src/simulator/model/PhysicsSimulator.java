package simulator.model;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Sergio Villarroel Fernández
 * 
 * The Class PhysicsSimulator.
 */
public class PhysicsSimulator {
	private List <SimulatorObserver> observadores;
	private GravityLaws law;
	private Double ts;
	private Double cont;
	private ArrayList<Body> listBod;

	/**
	 * Instantiates a new physics simulator.
	 *
	 * @param laws 
	 * @param t 
	 */
	public PhysicsSimulator(GravityLaws laws, double t){
		this.listBod= new ArrayList<Body>();
		this.cont=0.0;
		this.law=laws;
		this.ts=t;
		this.observadores = new ArrayList <SimulatorObserver>();

	}
	
	
	/**
	 * Adds the body.
	 *
	 * @param bod the bod
	 */
	public void addBody(Body bod){
		boolean salir=false;
		int i=0;

		while(!salir){
			if(i<=listBod.size()-1 && !listBod.isEmpty()){
				if(bod.equals(listBod.get(i).getId())){
					salir=true;
					
					throw new IllegalArgumentException("Illegal Body to add");
				}else{
					i++;
				}
			}else{
				salir=true;
			}

		}
		this.listBod.add(bod);
		for(SimulatorObserver ob : observadores){
			ob.onBodyAdded(this.listBod, bod);
		}

	}

	/**
	 * Advance.
	 */
	public void advance(){

		law.apply(listBod);
		for(int i=0 ; i<listBod.size();i++){
			
			listBod.get(i).move(this.ts);
			
			

		}
		
		cont = cont + this.ts;
		for(SimulatorObserver ob : observadores){
			ob.onAdvance(this.listBod, cont);
		}

	}


	public String toString(){
		char comillas = (char)34;
		return "{ "+comillas+"time"+comillas+": "+ cont+", " +comillas+"bodies"+comillas+": "+listBod.toString()+" }";


	}
public void reset(){
	
	this.ts = 0.0;
	this.cont = 0.0;
	this.listBod= new ArrayList<Body>();
	for(SimulatorObserver ob : observadores){
		ob.onReset(this.listBod, this.cont, this.ts, law.toString());
	}
}

public void setDeltaTime(double dt)throws IllegalArgumentException{
	
	this.ts = dt;
	for(SimulatorObserver ob : observadores){
		ob.onDeltaTimeChanged(dt);
	}
	
}

public void setGravityLaws(GravityLaws gravityLaws){
	if(gravityLaws == null){
		throw new IllegalArgumentException("Error to change the gravity law");
	}
	this.law = gravityLaws;
	for(SimulatorObserver ob : observadores){
		ob.onGravityLawChanged(this.law.toString());
	}
}

public void addObserver(SimulatorObserver o){
	observadores.add(o);	
	o.onRegister(this.listBod, this.cont, this.ts, law.toString());
	
}



}
