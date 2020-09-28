package simulator.factories;

public class IllegalArgumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public IllegalArgumentException(String name) {
		// TODO Auto-generated constructor stub
		super("Command error due to: "+ name);
	}
}
