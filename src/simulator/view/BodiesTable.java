package simulator.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class BodiesTable extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Controller ctrl;
	private BodiesTableModel TableModel;

	BodiesTable(Controller ctrl){

		this.ctrl = ctrl;
		InitGUI();
	}

	private void InitGUI() {

		TableModel  = new BodiesTableModel(ctrl); //instancia del modelo de la tabla
		setLayout(new BorderLayout()); // Layout de la tabla(BorderLayout)
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Bodies", TitledBorder.LEFT, TitledBorder.TOP)); // borde de la componente
		
		JTable tabla = new JTable(TableModel); // tabla creada con el modelo(TableModel)
		this.add(new JScrollPane(tabla)); // tabla añadida al Jpanel principal mediante un JscrollPane
		tabla.setFillsViewportHeight(true);
	}

}
