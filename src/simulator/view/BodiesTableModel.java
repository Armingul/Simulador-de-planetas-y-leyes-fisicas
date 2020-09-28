package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] NombresCol= {"Id", "Mass","Position","Velocity", "Acceleration"}; // nombres de las columnas
	private List<Body> _bodies;

	BodiesTableModel(Controller ctrl){
		_bodies = new ArrayList<>();
		ctrl.addObserver(this);
	}

	@Override
	// Numero de filas
	public int getRowCount() { 
		return _bodies.size();
	}

	@Override
	// numero de columnas
	public int getColumnCount() { 
		return NombresCol.length;
	}

	@Override
	// devuelve el nombre de la columna
	public String getColumnName(int column) {
		return NombresCol[column];
	}

	@Override
	// devuelve el valor que tiene la columna e
	public Object getValueAt(int rowIndex, int columnIndex) { 

		Body bod = _bodies.get(rowIndex);
		if(columnIndex == 0)
			return bod.getId();
		if(columnIndex == 1)
			return bod.getMass();
		if(columnIndex == 2)
			return bod.getPosition();
		if(columnIndex == 3)
			return bod.getVelocity();
		if(columnIndex == 4)
			return bod.getAcceleration();
		return null;
	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt,
			String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
			}
		});		


	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt,
			String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
			}
		});	


	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
			}
		});	



	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				fireTableStructureChanged();
			}
		});	



	}

	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}



}
