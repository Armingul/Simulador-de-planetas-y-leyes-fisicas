package simulator.model;

import simulator.misc.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class MassLossingBody.
 */
public class MassLossingBody extends Body{
	
	/** The loss factor. */
	private Double lossFactor; //entre 0 y 1, representa el factor de perdida de masa
	
	/** The loss frecuency. */
	private Double lossFrecuency; //Intervalo de tiempo despues del cual el objeto pierde masa
	
	/**
	 * Instantiates a new mass lossing body.
	 *
	 * @param id 
	 * @param mas 
	 * @param vel 
	 * @param acc 
	 * @param pos 
	 * @param lossFact 
	 * @param lossFrecu 
	 */
	public MassLossingBody (String id, Double mas, Vector vel, Vector acc , Vector pos, Double lossFact, Double lossFrecu){
		super(id,mas,vel,acc,pos);
		this.lossFactor=lossFact;
		this.lossFrecuency=lossFrecu;
	}
	
	/**
	 * Move.
	 *
	 * @param t 
	 */
	protected void  move(Double t) {
		double cont = 0.0;
		cont = t;
		
		super.move(cont);
		if(cont>=lossFrecuency){
			lossFactor=Math.floor(Math.random()*(1.0-0.0+1.0)+0.0); //Numero random entre 0.0 y 1.0
			super.setMass(super.getMass()*(1-lossFactor));
			cont=0.0;
		}
		
	}

}
