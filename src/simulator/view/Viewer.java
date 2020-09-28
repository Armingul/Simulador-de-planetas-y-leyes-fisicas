package simulator.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;
import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

public class Viewer extends JComponent implements SimulatorObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String cruz = "+";
	private int _centerX;
	private int _centerY;
	private double _scale;
	private List<Body> _bodies;
	private boolean _showHelp;
	static BufferedImage image = null;
	static BufferedImage image2 = null;
	static BufferedImage image3= null;
	static BufferedImage image4 = null;

	public Viewer(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		
		try {
			
			image = ImageIO.read(new File("src/planeta1.png"));
			image2 = ImageIO.read(new File("src/planeta2.png"));
			image3 = ImageIO.read(new File("src/planeta.png"));
			image4 = ImageIO.read(new File("src/planeta4.png"));
			Sound sound = new Sound();
			sound.sound();
			
			//gr.drawImage(image, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "Viewer", TitledBorder.LEFT, TitledBorder.TOP));
		_bodies = new ArrayList<>();
		_scale = 1.0;
		_showHelp = true;

		addKeyListener(new KeyListener() {
			// ...
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
				case '-': // boton para disminuir la escala del universo
					_scale = _scale * 1.1;
					break;
				case '+': // boton para aumentar la escala del universo
					_scale = Math.max(1000.0, _scale / 1.1);
					break;
				case '=': // boton para igualar la escala del universo a la dada por defecto
					autoScale();
					break;
				case 'h': // boton para mostrar/ocultar la ayuda
					_showHelp = !_showHelp;
					break;
				default:
				}
				repaint();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		addMouseListener(new MouseListener() {
		
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}

			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
	}
	@Override
	protected void paintComponent(Graphics g) {
		int cont = 0;
		double x, y;
		String id;

		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// use ’gr’ to draw not ’g’
		// calculate the center
		_centerX = getWidth() / 2;
		_centerY = getHeight() / 2;
		// TODO draw a cross at center
		gr.setColor(Color.BLUE);
		gr.drawString(cruz, _centerX, _centerY); // dibuja una cruz en el centro del viewer

		// TODO draw bodies

		while(cont < _bodies.size()) {
			// recorre la lista de cuerpos y los pinta.
			id = _bodies.get(cont).getId();
			x = (double) _bodies.get(cont).getPosition().coordinate(0); // coordenada x del cuerpo que va a ser pintado
			y = (double) _bodies.get(cont).getPosition().coordinate(1); // coordenada y del cuerpo que va a ser pintado
			if(cont == 0){
			gr.drawImage(image, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 30, 30, null);
			}
			else if(cont == 1){
				gr.drawImage(image2, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 30, 30, null);
			}
			else if(cont == 2){
				gr.drawImage(image3, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 30, 30, null);
			}
			else if(cont == 3){
				gr.drawImage(image4, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 30, 30, null);
			}
			else if(cont > 3){
				gr.drawImage(image2, _centerX + (int) (x/_scale), _centerY - (int) (y/_scale), 30, 30, null);
			}
			gr.setColor(Color.BLACK);
			gr.drawString(id, _centerX + (int) (x/_scale) + 3, _centerY - (int) (y/_scale) - 3); // pinta el id encima del cuerpo

			cont++;
		}


		// TODO draw help if _showHelp is true

		if(_showHelp) {
			String helpText = "h: toggle help, +: zoom in, -: zoom out, =: fit"; // texto de ayuda
			String scalingRatio = "ScalingRatio: " + _scale; // texto del ratio de la escala

			gr.setColor(Color.RED);
			gr.drawString(helpText, 10, 26);
			gr.drawString(scalingRatio, 10, 40);

		}

	}
	
	private void autoScale() {
		double max = 1.0;
		for (Body b : _bodies) {
			Vector p = b.getPosition();
			for (int i = 0; i < p.dim(); i++)
				max = Math.max(max,Math.abs(b.getPosition().coordinate(i)));
		}
		double size = Math.max(1.0, Math.min((double) getWidth(),
				(double) getHeight()));
		_scale = max > size ? 4.0 * max / size : 1.0;
	}
	@Override
	public void onRegister(java.util.List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});	


	}
	@Override
	public void onReset(java.util.List<Body> bodies, double time, double dt, String gLawsDesc) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});	



	}
	@Override
	public void onBodyAdded(java.util.List<Body> bodies, Body b) {

		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				autoScale();
				repaint();
			}
		});	


	}
	@Override
	public void onAdvance(java.util.List<Body> bodies, double time) {
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				_bodies = bodies;
				repaint();
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

