package simulator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;


public class StatusBar extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private JLabel _currTime;
	private JLabel _currLaws; 
	private JLabel _numOfBodies; 
	private Dimension dimension;

	public StatusBar(Controller ctrl) {
		initGUI();
		this._ctrl = ctrl;
		this._ctrl.addObserver(this);
	}
	
	private void initComponent(){
		
		_currTime=generarLabel(null);// for current time
		_currLaws=generarLabel(null);// for gravity laws
		_numOfBodies=generarLabel(null);// for number of bodies
		dimension = new Dimension(280,20);
		
		_currTime.setPreferredSize(dimension);
		_numOfBodies.setPreferredSize(dimension);
		_currLaws.setPreferredSize(dimension);
		
		_currTime.setText("Time: ");
		_numOfBodies.setText("Bodies: ");
		_currLaws.setText("Laws: ");
		
	}
	private void initGUI() {
		initComponent();
		
		this.setLayout( new FlowLayout( FlowLayout.LEFT ));
		this.setBorder( BorderFactory.createBevelBorder( 1 ));
		
		this.add(_currTime);
		this.add(_numOfBodies);
		this.add(_currLaws);
	}

	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_currTime.setText("Time: " + time);
				_currLaws.setText("Laws: " + gLawsDesc);
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});

	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_currTime.setText("Time: " + time);
				_currLaws.setText("Laws: " + gLawsDesc);
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});

	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});

	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_currTime.setText("Time: " + time);
				_numOfBodies.setText("Bodies: " + bodies.size());
			}
		});

	}
	@Override
	public void onDeltaTimeChanged(double dt) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_currLaws.setText("Laws: " + gLawsDesc);
			}
		});

	}
	
	private JLabel generarLabel(String info){
		return new JLabel(info);
	}

}
