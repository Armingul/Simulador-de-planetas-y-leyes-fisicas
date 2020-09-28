package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class ControlPanel extends JPanel implements SimulatorObserver {

	private static final long serialVersionUID = 1L;

	private Controller _ctrl;
	private JFileChooser fileChooser;
	private JButton LG;
	private JButton play;
	private JButton stop;
	private JButton load;
	private JButton exit;
	private JSpinner pasos;
	private JSpinner delay;
	private JLabel steps;
	private JLabel DeltaTime;
	private JTextField tiempo;
	 private Thread _thread;
	
	public ControlPanel(Controller ctrl) {
		this._ctrl = ctrl;
		initGUI();
		this._ctrl.addObserver(this);
		
	}
	
	private void initComponent(){
		
		LG = generarButton();
		play=generarButton();
		stop=generarButton();
		load=generarButton();
		exit=generarButton();
		fileChooser = generarFile();
		pasos = generarSpineer();
		steps = generarLabel("Steps: ");
		delay = generarSpineer();
		DeltaTime = generarLabel("Delta-Time: ");
		tiempo = generarJText(10);
	
	}

	private void initGUI() {
		
		initComponent();
		JToolBar toolBar2 = generarToolbar();
		JToolBar toolBar = generarToolbar();
		
		this.setLayout(new BorderLayout());
		
		

		//boton load
		load.setIcon(new ImageIcon("src/open.png"));
		load.setToolTipText("Seleccionar el fichero de los cuerpos");
		load.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cargaFichero();
			}
		});
		toolBar.add(load,BorderLayout.WEST);

		//boton leyes gravedad
		LG.setIcon(new ImageIcon("src/physics.png"));
		LG.setToolTipText("Seleccionar la ley de gravedad");
		LG.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				cambioLeyes();
			}
		});
		toolBar.add(LG,BorderLayout.WEST);
		
		
		

		//boton play
		play.setIcon(new ImageIcon("src/run.png"));
		play.setToolTipText("Ejecutar la simulacion");
		play.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(_thread == null) {
					_thread = new Thread(){
						public void run() {
							run_sim();

						}
					};
					_thread.start();

				}

			}
		});
		toolBar.add(play,BorderLayout.WEST);
		

		//boton stop
		stop.setIcon(new ImageIcon("src/stop.png"));
		stop.setToolTipText("Detener la simulacion");
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				_thread.interrupt();
				_thread = null;
			}
		});
		toolBar.add(stop,BorderLayout.WEST);
		
		//boton delay
		toolBar.add(new JLabel(" Delay: "));
		delay = new JSpinner(new SpinnerNumberModel(0,0,1000,1));
		delay.setToolTipText("pasos a ejecutar: 1-1000");
		delay.setValue(0);
		toolBar.add(delay);
		
		//steps
		toolBar.add(steps,BorderLayout.WEST);

		//selector de pasos
		SpinnerModel value = generarSpinner(10000,0,20000,100); 
		pasos.setModel(value);
		toolBar.add(pasos,BorderLayout.WEST);

		//delta-time
		Dimension dimension1 = new Dimension (40,40);
		DeltaTime.setMaximumSize(dimension1);
		toolBar.add(DeltaTime,BorderLayout.WEST);

		//selector de tiempo
		Dimension dimension2 = new Dimension (40,40);
		tiempo.setMaximumSize(dimension2);
		tiempo.setText("50000");
		toolBar.add(tiempo,BorderLayout.WEST);

		//boton salir
		exit.setIcon(new ImageIcon("src/exit.png"));
		load.setToolTipText("Cerrar el programa");
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				exit();
			}
		});

		toolBar2.add(exit,BorderLayout.EAST);
		//Añadir las componentes al frame
		this.add(toolBar,BorderLayout.WEST);
		this.add(toolBar2,BorderLayout.EAST);
	}

	private void exit() {
		int n = JOptionPane.showOptionDialog(new JFrame(),"Are sure you want to quit?", "Quit",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,null, null);
		if (n == 0) {
			System.exit(0); 
		}
	}



	private void cambioLeyes() {

		try {
			ArrayList<Object> array = new ArrayList<Object>();

			List<JSONObject> lista = _ctrl.getGravityLawsFactory().getInfo();
			for(JSONObject o : lista) {

				array.add(o.get("desc"));

			}
			String n = (String) JOptionPane.showInputDialog(
					this, // contenedor padre
					"Select Gravity Law to be used:", // mensaje en la ventana
					"Gravity Laws Selector", // etiqueta de la ventana
					JOptionPane.INFORMATION_MESSAGE, // icono seleccionado
					null, // icono seleccionado por el usuario (Icon)
					array.toArray(), // opciones para seleccionar
					"Newton's law of universal gravitation (nlug)");

			if (n.equals("Falling to center gravity")){
				this._ctrl.setGravityLaws(lista.get(0));
				System.out.println("Has elegido Ley de caida al centro");
			}
			else if (n.equals("Newton universal gravitation")) {
				this._ctrl.setGravityLaws(lista.get(1));
				System.out.println("Has elegido Ley de Newton");
			}
			else if (n.equals("No gravity")) {
				this._ctrl.setGravityLaws(lista.get(2));
				System.out.println("Has elegido Sin gravedad");
			}
			else System.out.println("Has cerrado sin elegir");

		}

		catch (simulator.factories.IllegalArgumentException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null,e);
		}
		catch (java.lang.NullPointerException e) {
			JOptionPane.showMessageDialog(null,e);

		}
	}

	public void cargaFichero(){

		int selection = fileChooser.showOpenDialog(load);
		if(selection == JFileChooser.APPROVE_OPTION){
			File file = fileChooser.getSelectedFile();

			System.out.println("loading: " + file.getName());
			try {
				this._ctrl.reset();
				this._ctrl.loadBodies(new FileInputStream(file));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			} catch (simulator.factories.IllegalArgumentException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null,e);
			}
		}
		else{
			System.out.println("load cancelled by user");
		}
	}

	public void run_sim() {
		try {

			load.setEnabled(false);
			play.setEnabled(false);
			LG.setEnabled(false);
			pasos.setEnabled(false);
			tiempo.setEnabled(false);
			exit.setEnabled(false);
			_ctrl.setDeltaTime(Double.parseDouble(tiempo.getText()));
			int i=0;
			while(i < (Integer)pasos.getValue() && !Thread.currentThread().isInterrupted() ) {
				_ctrl.run(1);
				i++;
				try {
					Thread.sleep((Integer)delay.getValue());
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
					load.setEnabled(true);
					play.setEnabled(true);
					stop.setEnabled(true);
					LG.setEnabled(true);
					pasos.setEnabled(true);
					tiempo.setEnabled(true);
					exit.setEnabled(true);

				}
			}

			load.setEnabled(true);
			play.setEnabled(true);
			stop.setEnabled(true);
			LG.setEnabled(true);
			pasos.setEnabled(true);
			tiempo.setEnabled(true);
			exit.setEnabled(true);
		}catch(ArrayIndexOutOfBoundsException | IOException e) {
			System.out.println("Fallo en la ejecucion. ");
		}

	}


	@Override
	public void onRegister(List<Body> bodies, double time, double dt,String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				tiempo.setText(Double.toString(dt));
			}
		});

	}
	@Override
	public void onReset(List<Body> bodies, double time, double dt,String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				tiempo.setText(Double.toString(dt));
			}
		});

	}
	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onAdvance(List<Body> bodies, double time) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onDeltaTimeChanged(double dt) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				tiempo.setText(Double.toString(dt));
			}
		});


	}
	@Override
	public void onGravityLawChanged(String gLawsDesc) {
		// TODO Auto-generated method stub

	}

	private JButton generarButton(){
		return new JButton();
	}

	private JToolBar generarToolbar(){
		return new JToolBar();
	}

	private JFileChooser generarFile(){
		return  new JFileChooser();
	}

	private JSpinner generarSpineer(){
		return new JSpinner();
	}

	private JLabel generarLabel(String info){
		return new JLabel(info);
	}

	private JTextField generarJText(int n){
		return new JTextField(n);
	}

	private SpinnerNumberModel generarSpinner(int x, int y, int w, int z){
		return new SpinnerNumberModel(x,y,w,z); 
	}
}