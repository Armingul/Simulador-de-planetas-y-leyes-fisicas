package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simulator.control.Controller;


public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7635931122303126013L;
	// Añade atributos para todos los componentes (clases)
	private Controller _ctrl;
	private ControlPanel panel;
	private StatusBar barraEstado;
	private BodiesTable TablaCuerpos;
	private Viewer Visor;

	public MainWindow(Controller ctrl) {
		super("Physics Simulator");
		this._ctrl = ctrl;
		panel = new ControlPanel(_ctrl);
		barraEstado = new StatusBar(_ctrl);
		TablaCuerpos = new BodiesTable(_ctrl);
		Visor = new Viewer(_ctrl);
		initGUI();
	}
	private void initGUI() {
		Dimension dimension = new Dimension (0,400);

		JPanel mainPanel = new JPanel(new BorderLayout());
		this.setContentPane(mainPanel);
	
		TablaCuerpos.setPreferredSize(dimension);
		Visor.setPreferredSize(dimension);
		JPanel PanelCentral = new JPanel();
		PanelCentral.setLayout(new BoxLayout(PanelCentral,BoxLayout.Y_AXIS));
		PanelCentral.add(TablaCuerpos);
		PanelCentral.add(Visor);

		mainPanel.add(panel,BorderLayout.PAGE_START);
		mainPanel.add(barraEstado, BorderLayout.PAGE_END);
		mainPanel.add(PanelCentral,BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
}
