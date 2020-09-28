package simulator.launcher;

/*
 * Examples of command-line parameters:
 * 
 *  -h
 *  -i resources/examples/ex4.4body.txt -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl ftcg
 *  -i resources/examples/ex4.4body.txt -o resources/examples/ex4.4body.out -s 100 -gl nlug
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import extra.json.MyStringUtils;
import simulator.control.Controller;
import simulator.factories.BasicBodyBuilder;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.FallingToCenterGravityBuilder;
import simulator.factories.MassLosingBodyBuilder;
import simulator.factories.NewtonUniversalGravitationBuilder;
import simulator.factories.NoGravityBuilder;
import simulator.model.Body;
import simulator.model.GravityLaws;
import simulator.model.PhysicsSimulator;
import simulator.view.MainWindow;


// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main {

	// default values for some parameters
	/** The Constant _dtimeDefaultValue. */
	//
	private final static Double _dtimeDefaultValue = 2500.0;

	// some attributes to stores values corresponding to command-line parameters
	/** The dtime. */
	//
	private static Double _dtime = null;

	/** The in file. */
	private static String _inFile = null;

	/** The gravity laws info. */
	private static JSONObject _gravityLawsInfo = null;

	/** The fichero entrada. */
	private static String ficheroEntrada = null;

	/** Modo del simulador. */
	private static String mode = null;

	/** The nombre fichero. */
	private static String nombreFichero = null;

	/** The fichero salida. */
	private static String ficheroSalida = null;

	/** The filename confirmed. */
	private static Boolean filename_confirmed;

	/** The steps. */
	private static int _steps =0;

	/** The Constant filenameInUseMsg. */
	public static final String filenameInUseMsg= "The file already exists ; do you want to overwrite it ? (Y/N)";

	/** The body factory. */
	// factories
	private static Factory<Body> _bodyFactory;

	/** The gravity laws factory. */
	private static Factory<GravityLaws> _gravityLawsFactory;




	/**
	 * Inits the.
	 */
	private static void init() {

		ArrayList<Builder<Body>> bodyBuilders = new ArrayList<>();
		ArrayList<Builder<GravityLaws>> lawsBuilders = new ArrayList<>();

		bodyBuilders.add(new MassLosingBodyBuilder());
		bodyBuilders.add(new BasicBodyBuilder());

		lawsBuilders.add(new FallingToCenterGravityBuilder());
		lawsBuilders.add(new NewtonUniversalGravitationBuilder());
		lawsBuilders.add(new NoGravityBuilder());

		_bodyFactory = new BuilderBasedFactory<Body>(bodyBuilders);
		_gravityLawsFactory = new BuilderBasedFactory<GravityLaws>(lawsBuilders);


	}

	/**
	 * Parses the args.
	 *
	 * @param args the args
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws JSONException 
	 */
	private static void parseArgs(String[] args) throws JSONException, simulator.factories.IllegalArgumentException {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseDeltaTimeOption(line);
			parseGravityLawsOption(line);
			parseOutPut(line);
			parseSteps(line);
			parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	/**
	 * Builds the options.
	 *
	 * @return the options
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws JSONException 
	 */
	private static Options buildOptions() throws JSONException, simulator.factories.IllegalArgumentException {
		Options cmdLineOptions = new Options();


		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Bodies JSON output file.").build());

		//print mode
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("Simulator mode").build());

		// steps time
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("Steps time.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ _dtimeDefaultValue + ".")
						.build());

		// gravity laws -- there is a workaround to make it work even when
		// _gravityLawsFactory is null. 
		//
		String gravityLawsValues = "N/A";
		String defaultGravityLawsValue = "N/A";
		if (_gravityLawsFactory != null) {
			gravityLawsValues = "";
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gravityLawsValues.length() > 0) {
					gravityLawsValues = gravityLawsValues + ", ";
				}
				gravityLawsValues = gravityLawsValues + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
			}
			defaultGravityLawsValue = _gravityLawsFactory.getInfo().get(0).getString("type");
		}
		cmdLineOptions.addOption(Option.builder("gl").longOpt("gravity-laws").hasArg()
				.desc("Gravity laws to be used in the simulator. Possible values: " + gravityLawsValues
						+ ". Default value: '" + defaultGravityLawsValue + "'.")
						.build());

		return cmdLineOptions;
	}

	/**
	 * Parses the help option.
	 *
	 * @param line the line
	 * @param cmdLineOptions the cmd line options
	 */
	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	/**
	 * Parses the in file option.
	 *
	 * @param line the line
	 * @throws ParseException the parse exception
	 */
	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		ficheroEntrada=_inFile;
		if (_inFile == null) {
			throw new ParseException("An input file of bodies is required");
		}
	}

	/**
	 * Parses the out put.
	 *
	 * @param line the line
	 * @throws ParseException the parse exception
	 */
	private static void parseOutPut(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("o");
		ficheroSalida=_inFile;
		if (_inFile == null) {
			throw new ParseException("Salida de fichero");
		}
	}

	private static void parseModeOption(CommandLine line) throws ParseException {
		mode = line.getOptionValue("m");

		if (mode == null) {
			throw new ParseException("A valid mode is required");
		}
	}

	/**
	 * Parses the delta time option.
	 *
	 * @param line the line
	 * @throws ParseException the parse exception
	 */
	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", _dtimeDefaultValue.toString());
		try {
			_dtime = Double.parseDouble(dt);
			assert (_dtime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	/**
	 * Parses the steps.
	 *
	 * @param line the line
	 * @throws ParseException the parse exception
	 */
	private static void parseSteps(CommandLine line) throws ParseException {
		String steps  = line.getOptionValue("s");
		try {
			_steps = Integer.parseInt(steps);
			assert (_steps > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid steps value: " + _steps);
		}
	}

	/**
	 * Parses the gravity laws option.
	 *
	 * @param line the line
	 * @throws ParseException the parse exception
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws JSONException 
	 */
	private static void parseGravityLawsOption(CommandLine line) throws ParseException, JSONException, simulator.factories.IllegalArgumentException {

		// this line is just a work around to make it work even when _gravityLawsFactory
		// is null, you can remove it when've defined _gravityLawsFactory
		if (_gravityLawsFactory == null)
			return;

		String gl = line.getOptionValue("gl");
		if (gl != null) {
			for (JSONObject fe : _gravityLawsFactory.getInfo()) {
				if (gl.equals(fe.getString("type"))) {
					_gravityLawsInfo = fe;
					break;
				}
			}
			if (_gravityLawsInfo == null) {
				throw new ParseException("Invalid gravity laws: " + gl);
			}
		} else {
			_gravityLawsInfo = _gravityLawsFactory.getInfo().get(0);
		}
	}

	/**
	 * @author Sergio Villarroel Fernández
	 * 
	 * Confirm file name string for write.
	 *
	 * @param filenameString the filename string
	 * @return the string
	 * @throws Exception the exception
	 */
	private static String confirmFileNameStringForWrite(String filenameString) throws Exception{

		String loadName = filenameString; 
		filename_confirmed = false;
		while (!filename_confirmed) {
			if(loadName != null) {
				if (MyStringUtils.isValidFilename(loadName)) {
					File file = new File(loadName);
					if (!file .exists())
						filename_confirmed = true; 
					else {
						loadName = getLoadName(filenameString); 
					}
				}else throw new Exception("Bad name of file"); 
			}else filename_confirmed=true;
		}
		return loadName;
	}

	/**
	 * @author Sergio Villarroel Fernández
	 * 
	 * Gets the load name.
	 *
	 * @param filenameString the filename string
	 * @return the load name
	 */
	public static String getLoadName(String filenameString) { 

		String newFilename = null;
		boolean yesOrNo = false;

		while (!yesOrNo) {
			System.out.print(filenameInUseMsg + ": ");

			@SuppressWarnings("resource")
			Scanner in = new Scanner (System.in);

			String[] responseYorN = in.nextLine().toLowerCase().trim().split("\\s+"); 
			if (responseYorN.length == 1) {
				switch (responseYorN[0]) { 
				case "y":
					yesOrNo = true;
					File Fichero = new File(filenameString);
					Fichero.delete();
					return filenameString;
				case "n":
					yesOrNo = true;
					System.out.println("Enter other name of file: ");
					newFilename= in.nextLine();
					File Fich = new File(newFilename);
					Fich.delete();
					return newFilename;

				default:
					System.out.println("do you want to overwrite it ? (Y/N)");
				}
			} else {
				System.out.println("Please enter only one character (Y/N).");
			} }
		return newFilename; }

	/**
	 * Take law.
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws JSONException 
	 */
	private static void takeLaw() throws JSONException, simulator.factories.IllegalArgumentException {

		for (JSONObject fe : _gravityLawsFactory.getInfo()) {
			if (_gravityLawsInfo.get("type").equals(fe.getString("type"))) {
				_gravityLawsInfo = fe;
				break;
			}
		}
	}

	/**
	 * @author Sergio Villarroel Fernández
	 * 
	 * Start batch mode.
	 * @throws simulator.factories.IllegalArgumentException 
	 * @throws InterruptedException 
	 * @throws InvocationTargetException 
	 *
	 * @throws Exception the exception
	 */
	private static void startINITMode() throws simulator.factories.IllegalArgumentException, InvocationTargetException, InterruptedException{

		init();
		takeLaw();

		GravityLaws la = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		if(la!=null){
			try{
				PhysicsSimulator simulator = new PhysicsSimulator(la, _dtime);
				Controller controll = new Controller(simulator, _bodyFactory, _gravityLawsFactory);

				//prueba del panel

				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						new MainWindow(controll);
					}
				});
			}catch(IllegalArgumentException e){
				throw new IllegalArgumentException("Fallo en startINITMode");
			}
		}
	}

	private static void startBatchMode() throws Exception {
		// create and connect components, then start the simulator		

		@SuppressWarnings("resource")
		Scanner in =  new Scanner (System.in);
		String pedirComando;
		if(ficheroSalida==null){
			pedirComando = "Introduzca nombre de archivo: ";
		}else{
			pedirComando = "Introduzca nombre de archivo: , si no sera guardado en " + ficheroSalida;
		}

		InputStream is = new FileInputStream(new File(ficheroEntrada));
		OutputStream os =  Main.ficheroSalida == null ? System.out : new FileOutputStream(new File(ficheroSalida));

		init();
		takeLaw();

		GravityLaws la = _gravityLawsFactory.createInstance(_gravityLawsInfo);
		if(la!=null){
			try{
				PhysicsSimulator simulator = new PhysicsSimulator(la, _dtime);
				Controller controll = new Controller(simulator, _bodyFactory, _gravityLawsFactory);

				controll.loadBodies(is);

				System.out.println(pedirComando);
				nombreFichero = in.nextLine();

				if(nombreFichero.equals("") && !ficheroSalida.equals("")){

					guardadoArchivo(controll);

				}else{

					if(nombreFichero.equals("")&& ficheroSalida.equals("")){
						System.out.println("No se ha especificado archivo de guardado. \n");
						controll.run(_steps,os);
					}else{
						guardadoArchivo(controll);
					}

				}

				is.close();
				os.close();

			}catch(IllegalArgumentException e){
				throw new IllegalArgumentException("Fallo en startBatchMode");
			}

		}else{
			is.close();
			os.close();
			throw new IllegalArgumentException("GravityLaws is null");
		}


	}






	/**
	 * @author Sergio Villarroel Fernández
	 * 
	 * Guardado archivo.
	 *
	 * @param control the control
	 * @throws Exception the exception
	 */
	private static void guardadoArchivo(Controller control) throws Exception{

		BufferedWriter output=null;

		if(!confirmFileNameStringForWrite(nombreFichero).equals("")){
			output= new BufferedWriter(new FileWriter(nombreFichero));

			if(control.runBuffered(_steps, output)) {
				output.close();
				System.out.println("Game successfully saved in file "+nombreFichero);
			}else throw new Exception("Save error");


		}else if(ficheroSalida != null){
			output= new BufferedWriter(new FileWriter(ficheroSalida));


			if(control.runBuffered(_steps, output)) {

				System.out.println("Game successfully saved in file "+ficheroSalida);
				output.close();
			}else throw new Exception("Save error");

		}






	}

	/**
	 * @author Sergio Villarroel Fernández
	 * 
	 * Start.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	private static void start(String[] args) throws Exception {

		parseArgs(args);

		if (mode.equals("gui")) {
			startINITMode();
		}
		else if(mode.equals("batch")) {
			startBatchMode();
		}

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
