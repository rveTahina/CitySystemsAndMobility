/*
©Copyright 2012 Nick Malleson
This file is part of RepastCity.

RepastCity is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

RepastCity is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with RepastCity.  If not, see <http://www.gnu.org/licenses/>.
 */

package repastcity3.agent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.vividsolutions.jts.geom.Coordinate;

import repast.simphony.engine.environment.RunListener;
import repast.simphony.engine.environment.RunState;
import repast.simphony.userpanel.ui.UserPanelCreator;
import repastcity3.environment.Building;
import repastcity3.environment.Route;
import repastcity3.exceptions.NoIdentifierException;
import repastcity3.main.ContextManager;

public class DefaultAgent extends AgentClass{

	private static Logger LOGGER = Logger.getLogger(DefaultAgent.class.getName());

	private Building home; // Where the agent lives
	private Route route; // An object to move the agent around the world

	private boolean goingHome = false; // Whether the agent is going to or from their home

	private static int uniqueID = 0;
	private int id;

	//===================================================================================================================================================================
	private static int compteur=0;

	private List<Coordinate> currentCoord;// list that contain the coordinate of the current position of the agent
	private List<Instant> currentTimeStamp;// list that contain the cuurent TimeStamp


	private ArrayList<List<Coordinate>> agentPath=new ArrayList<List<Coordinate>>();//ArrayList that content the trajectory of the agent

	private ArrayList<List<Instant>> allTimeStamps=new ArrayList<List<Instant>>();//ArrayList of all timestamp
	//====================================================================================================================================================================

	public DefaultAgent() {
		
		this.id = uniqueID++;
	}

	@Override
	public void step() throws Exception {

		/*LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home
			// Choose a new building to go to
			Building b = ContextManager.buildingContext.getRandomObject();
			this.route = new Route(this, b.getCoords(), b);
			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());
		}
		if (!this.route.atDestination()) {
			this.route.travel();
			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {
			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				Building b = ContextManager.buildingContext.getRandomObject();
				this.route = new Route(this, b.getCoords(), b);
				LOGGER.log(Level.FINE, this.toString() + " reached home, now going to " + b.toString());
			} else {
				LOGGER.log(Level.FINE, this.toString() + " reached " + this.route.getDestinationBuilding().toString()
						+ ", now going home");
				this.goingHome = true;
				this.route = new Route(this, this.home.getCoords(), this.home);
			}

		}*/

		//==========================================================================================================================
		Building b= ContextManager.buildingContext.getRandomObject();

		// TODO Auto-generated method stub
		LOGGER.log(Level.FINE, "Agent " + this.id + " is stepping.");
		if (this.route == null) {
			this.goingHome = false; // Must be leaving home

			b = randomBuilding(3); // Choose a new building to go to . rendomBuilding(Commune)


			this.route = new Route(this, b.getCoords(), b);


			LOGGER.log(Level.FINE, this.toString() + " created new route to " + b.toString());
		}

		if (!this.route.atDestination()) {
			this.route.travel();
			
//====================================================================================================================================================

			setCurrentRoute();//Pick up the trajectory of the agent from his current place to his destination
			setCurrentTimeStamp();// Pick up the timestamp from the agent's current place to his destination

			setagentPath(compteur);//Pick up all the agent trajectory during the simulation
			setAllTimeStamp(compteur,currentTimeStamp);//pick up all the agent timestamp during the simulation
			
			
			ContextManager.setDefaultAgentPath("DefaultAgent"+this.toString(), agentPath);
			ContextManager.setDefaultAgentTimeStamp("DefaultAgent"+this.toString(), allTimeStamps);

			//=========================================================================================================================================

			LOGGER.log(Level.FINE, this.toString() + " travelling to " + this.route.getDestinationBuilding().toString());
		} else {


			compteur++;

			// Have reached destination, now either go home or onto another building
			if (this.goingHome) {
				this.goingHome = false;
				b= randomBuilding(3); // Choose a new building to go to . rendomBuilding(Commune)
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
		//serialiseMe(); // serialise this agent


	} // step()

	/**
	 * There will be no inter-agent communication so these agents can be executed simulataneously in separate threads.
	 */
	@Override
	public final boolean isThreadable() {
		return true;
	}

	@Override
	public void setHome(Building home) {
		this.home = home;
	}

	@Override
	public Building getHome() {
		return this.home;
	}

	@Override
	public <T> void addToMemory(List<T> objects, Class<T> clazz) {
	}

	@Override
	public List<String> getTransportAvailable() {
		return null;
	}

	@Override
	public String toString() {
		return "Agent " + this.id;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DefaultAgent))
			return false;
		DefaultAgent b = (DefaultAgent) obj;
		return this.id == b.id;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	//=======================================================================================================================================================================
	//Choose a random building
	public Building randomBuilding(int a) throws NumberFormatException, NoIdentifierException{
		Building b;
		do{
			b = ContextManager.buildingContext.getRandomObject();

		} while(b.getCommune()!=a);

		return b;
	}

	//set the current agent trajectory
	public void setCurrentRoute() {

		this.currentCoord=this.route.getListRoute();

	}
	
	@Override
	public void getCurrentRoute() {
		this.currentCoord=this.route.getListRoute();

	}


	//set the current agent timestamp(for the KML export)
	public void setCurrentTimeStamp(){
		this.currentTimeStamp=this.route.getTimeStamp();
	}

	public void setAllTimeStamp(int compt, List<Instant> currentTimeStamp2){
		if (this.allTimeStamps.size()==compt){
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

	public void setagentPath(int compt) throws FileNotFoundException{

		if (this.agentPath.size()==compt){
			this.agentPath.add(currentCoord);

		}
		else{
			this.agentPath.set(this.agentPath.size()-1,currentCoord);
		}

	}

	@Override
	public ArrayList<List<Coordinate>> getagentPath() {

		return this.agentPath;
	}

	//Serialise this agent
	@Override
	public void serialiseMe() {
		// TODO Auto-generated method stub
		try
		{
			/*FileOutputStream fileOut = new FileOutputStream("Default"+this.toString()+".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();*/
			/*OutputStream file = new FileOutputStream("Default"+this.toString()+".ser");
		      OutputStream buffer = new BufferedOutputStream(file);
		      ObjectOutput output = new ObjectOutputStream(buffer);
		      output.writeObject(this);*/
			ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(new File("Default"+this.toString()+".ser"))));
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

	


}
