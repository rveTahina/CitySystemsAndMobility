package repastcity3.agent;




import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.userpanel.ui.UserPanelCreator;
import repastcity3.environment.Building;
import repastcity3.environment.Route;
import repastcity3.exceptions.NoIdentifierException;
import repastcity3.main.ContextManager;
import repastcity3.main.exportIntoKML;
import repastcity3.main.intoKml;


public class Student extends AgentClass {

	private static Logger LOGGER = Logger.getLogger(DefaultAgent.class.getName());

	private Building home; // Where the agent lives
	private Route route; // An object to move the agent around the world

	private boolean goingHome = false; // Whether the agent is going to or from their home

	public static int uniqueID = 0;
	private int id;
	

	//=====================================================================================================================================================================================
	private static int compteur=0;


	private List<Coordinate> currentCoord;//list that contain the coordinates of the current position of the agent
	private List<Instant> currentTimeStamp;//list that contain the Instant


	private ArrayList<List<Coordinate>> agentPath=new ArrayList<List<Coordinate>>();//ArrayList that content the trajectory of the agent
	private ArrayList<List<Instant>> allTimeStamps=new ArrayList<List<Instant>>();//ArrayList of all timestamp
	
	String StudentColor;



	//=====================================================================================================================================================================================

	public Student(){
		//RunState.getInstance().getScheduleRegistry().getScheduleRunner().addRunListener(this);

		this.id = uniqueID++;
		StudentColor=generateColor();

	}

	@Override
	public void step() throws Exception {

		
		
		Building b= ContextManager.buildingContext.getRandomObject();


		// TODO Auto-generated method stub
		LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home

			//===================================================================================================================================
			b = randomBuilding(1); // Choose a new building to go to . randomBuilding (Commune)
			//===================================================================================================================================


			this.route = new Route(this, b.getCoords(), b);



			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());

		}

		if (!this.route.atDestination()) {
			this.route.travel();

			//==================================================================================================================================
			setCurrentRoute();//Pick up the trajectory of the agent from his current place to his destination
			setCurrentTimeStamp();//Pick up the current time stamp

			setagentPath(compteur);
			setAllTimeStamp(compteur,currentTimeStamp);

			ContextManager.setStudentPath("Student"+this.toString(), agentPath);
			ContextManager.setStudentTimeStamp("Student"+this.toString(), allTimeStamps);

			setagentPath(compteur);
			setAllTimeStamp(compteur,currentTimeStamp);
			
			System.out.println("AGENTPATH::::"+this.getagentPath());
			System.out.println("TIMESTAMP:::::"+this.getAllTimeStamp());
			
			

			//intoKml exportSimulation=new intoKml(10); // export the simulation into .kml for live simulation in google earth: intoKml(time Stamp)
			//exportSimulation.go();

			//if((this.agentPath.size()-1) % 10==0){
			//System.out.println("This.agentpath: "+agentPath.size());
			//intoKml exportSimulation=new intoKml(10); // export the simulation into .kml for live simulation in google earth: intoKml(time Stamp)
			//exportSimulation.go();
			//	}



			//====================================================================================================================================

			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {


			compteur++; // Tah

			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				b= randomBuilding(1); // Tah: randomBuilding(a): Choose a random building among a set of buildings with commune = a . 

				this.route = new Route(this, b.getCoords(), b);


				System.out.println(this.toString() + " reached home, now going to " + b.toString());
				LOGGER.log(Level.FINE, this.toString() + " reached home, now going to " + b.toString());
			} else {
				System.out.println(this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				LOGGER.log(Level.FINE, this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				this.goingHome = true;
				this.route = new Route(this, this.home.getCoords(), this.home);


			}


		}

		
		ContextManager.getOos().writeUnshared(this);
		ContextManager.getOos().reset();

	}

	//============================================================================================================================================================================

	//Method for the choice of the destination building
	public Building randomBuilding(int a) throws NumberFormatException, NoIdentifierException{
		Building b;
		do{
			b = ContextManager.buildingContext.getRandomObject();

		} while(b.getCommune()!=a);

		return b;	

	}

	//Pick up the trajectory of the agent from his current place to his destination
	public void setCurrentRoute() {

		this.currentCoord=this.route.getListRoute();

	}

	@Override
	public void getCurrentRoute() {
		// TODO Auto-generated method stub

	}

	public void setCurrentTimeStamp(){
		this.currentTimeStamp=this.route.getTimeStamp();
	}

	public void setAllTimeStamp(int compt, List<Instant> currentTimeStamp2){
		if (this.allTimeStamps.size()==compt || this.allTimeStamps.size()==0){
			this.allTimeStamps.add(currentTimeStamp2);
		}
		else
		{
			this.allTimeStamps.set(this.allTimeStamps.size()-1,currentTimeStamp2);	

		}

	}

	public ArrayList<List<Instant>> getAllTimeStamp(){
		return this.allTimeStamps;
	}

	//Store all the trajectory of the agent inside an arrayList
	public void setagentPath(int compt) throws FileNotFoundException{
		//List<Coordinate> current;
		if (this.agentPath.size()==compt || this.agentPath.size()==0){
			this.agentPath.add(currentCoord);

		}
		else{

			this.agentPath.set(this.agentPath.size()-1,currentCoord);

		}

	}

	//Return the arrayList of all the agent trajectories
	public ArrayList<List<Coordinate>> getagentPath(){
		return this.agentPath;

	}

	public void serialiseMe(){ 
		try
		{
			/*FileOutputStream fileOut = new FileOutputStream("Student"+this.toString()+".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();*/

			ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("Student"+this.toString()+".ser"))));
			//OutputStream buffer = new BufferedOutputStream(file);
			//ObjectOutput output = new ObjectOutputStream(buffer);
			//output.writeObject(this);
			file.writeObject(this);
			file.close();

		}catch(IOException i)
		{
			i.printStackTrace();

		}
	}




	//===================================================================================================================================================

	@Override
	public boolean isThreadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHome(Building home) {
		// TODO Auto-generated method stub
		this.home=home;

	}

	@Override
	public Building getHome() {
		// TODO Auto-generated method stub
		return this.home;
	}

	@Override
	public <T> void addToMemory(List<T> objects, Class<T> clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getTransportAvailable() {
		// TODO Auto-generated method stub
		return null;
	}

	public String toString() {
		return "Agent " + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Student))
			return false;
		Student b = (Student) obj;
		return this.id == b.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}
	
	public static String generateColor(){
		int red = (int) (( Math.random()*255)+1);
		int green = (int) (( Math.random()*255)+1);
		int blue = (int) (( Math.random()*255)+1);
		Color RandomC = new Color(red,green,blue);
		int RandomRGB = (RandomC.getRGB());
		String RandomRGB2Hex = Integer.toHexString(RandomRGB);
		return RandomRGB2Hex;
	}
	
	public String getColor(){
		return StudentColor;
	}
	
	


	/*	
	@ScheduledMethod(start = 50, interval = 100)
	public void exp(){
		exportIntoKML exportSimulation=new exportIntoKML(10);
		try {
			exportSimulation.go();
			System.out.println("ooooooooooooooooooooooooooo");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	 */



}
