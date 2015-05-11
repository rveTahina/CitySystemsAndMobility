/**
 * @author tahina RALITERA
 */
package repastcity3.agent;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import repastcity3.environment.Building;
import repastcity3.exceptions.NoIdentifierException;

import com.vividsolutions.jts.geom.Coordinate;

public class AgentClass implements IAgent, java.io.Serializable{

	@Override
	public void step() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isThreadable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setHome(Building home) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Building getHome() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public Building randomBuilding(int a) throws NumberFormatException,
			NoIdentifierException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getCurrentRoute() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setagentPath(int compt) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<List<Coordinate>> getagentPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void serialiseMe() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<List<Instant>> getAllTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

}
