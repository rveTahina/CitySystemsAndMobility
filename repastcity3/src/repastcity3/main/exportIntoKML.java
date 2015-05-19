/**
 * @author tahina RALITERA
 * This class is to export the simulation to .kml file for visualisation into Google Earth
 * It uses the JAK (Java API for KML) http://labs.micromata.de/projects/jak.html library
 * */

package repastcity3.main;

import java.io.*;
import java.time.Instant;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import de.micromata.opengis.kml.v_2_2_0.Units;
import de.micromata.opengis.kml.v_2_2_0.Vec2;
import repastcity3.agent.AgentFactory;



public class exportIntoKML{
	static int compteurStudent, compteurAgent;
	int pas;

	public exportIntoKML(int param){
		if(param==0){
			pas=1;
		}
		else{
			pas=param;
		}
		System.out.println("Export into KML, time stamp: "+param);

	}


	public void go() throws FileNotFoundException{

		//Student s = new Student();
		//DefaultAgent d =new DefaultAgent();

		de.micromata.opengis.kml.v_2_2_0.Kml kml = de.micromata.opengis.kml.v_2_2_0.KmlFactory.createKml();
		java.time.Instant dt = null;

		//add the link 
		//de.micromata.opengis.kml.v_2_2_0.Link lnk=kml.createAndSetNetworkLink().createAndSetLink();
		de.micromata.opengis.kml.v_2_2_0.Link lnk = new de.micromata.opengis.kml.v_2_2_0.Link();
		lnk.setHref("F:/ESIROI/Stage/Projet/CitySystemsAndMobility/repastcity3/map.kml");
		lnk.setRefreshMode(RefreshMode.ON_INTERVAL);
		lnk.setRefreshInterval(10);
		/*lnk.setViewRefreshMode(ViewRefreshMode.ON_REGION);
		lnk.setViewRefreshTime(1);*/


		//Create the document tag
		de.micromata.opengis.kml.v_2_2_0.Document document = kml.createAndSetDocument().withName("MyMarkers");

		//document.createAndAddNetworkLink().setLink(lnk);


		List<com.vividsolutions.jts.geom.Coordinate> current;
		List<Instant> currentListTmstp;
		compteurStudent=AgentFactory.nbrStudent;
		compteurAgent=AgentFactory.nbrDefaultAgent;




		//=========================================Student==========================================
		for(int l=0;l<compteurStudent;l++)
		{

			String couleurStudent;

			if(ContextManager.getStudentAgentColor().size()==compteurStudent){
				couleurStudent=ContextManager.getStudentAgentColor().get(l);
			}
			else{
				couleurStudent=ContextManager.getStudentAgentColor().get(l-1);
			}

			for(int i = 0; i < ContextManager.getStudentPath().get("StudentAgent "+l).size(); i++) {
				current=ContextManager.getStudentPath().get("StudentAgent "+l).get(i);
				currentListTmstp=ContextManager.getStudentTimeStamp().get("StudentAgent "+l).get(i);
				int a=0;

				for(int j = 0; j < current.size(); j=j+pas){

					export("http://maps.google.com/mapfiles/ms/icons/cabs.png",current, currentListTmstp, dt, document, couleurStudent, j, a);
				}
			}
		}


		//=====================DefaultAgent=============================================================================
		for(int m=0;m<compteurAgent;m++){
		
			String couleurDefaultAgent;

			if(ContextManager.getDefaultAgentColor().size()==compteurAgent){
				couleurDefaultAgent=ContextManager.getDefaultAgentColor().get(m);
			}
			else{
				System.out.println("Compt:"+compteurAgent);
				System.out.println("SIZE: "+ContextManager.getDefaultAgentColor().size());
				couleurDefaultAgent=ContextManager.getDefaultAgentColor().get(m-1);
			}

			for(int i = 0; i < ContextManager.getDefaultAgentPath().get("DefaultAgentAgent "+m).size(); i++) {
				current=ContextManager.getDefaultAgentPath().get("DefaultAgentAgent "+m).get(i);
				currentListTmstp=ContextManager.getDefaultAgentTimeStamp().get("DefaultAgentAgent "+m).get(i);
				int a=0;

				for(int j = 0; j < current.size(); j=j+pas){

					export("http://maps.google.com/mapfiles/kml/shapes/woman.png",current, currentListTmstp, dt, document, couleurDefaultAgent, j, a);
				}
			}

		}


		kml.marshal(new File("Simulation.kml"));
		System.out.println("==========================Export done=====================");




	}


	public void export(String link,List<com.vividsolutions.jts.geom.Coordinate> current,List<Instant> currentListTmstp, java.time.Instant dt, de.micromata.opengis.kml.v_2_2_0.Document document, String couleur, int j, int a){


		Placemark placemark = KmlFactory.createPlacemark();
		//placemark.setName(s.toString());
		//placemark.setVisibility(true);
		//placemark.setOpen(false);
		//placemark.setDescription("Un placemarque");
		//placemark.setStyleUrl("styles.kml#jugh_style");


		// Create <Point> and set values.
		de.micromata.opengis.kml.v_2_2_0.Point point = KmlFactory.createPoint();
		//point.setExtrude(false);
		//point.setAltitudeMode(AltitudeMode.CLAMP_TO_GROUND);


		point.getCoordinates().add(new Coordinate(current.get(j).x,current.get(j).y));
		placemark.setGeometry(point);

		
		dt=currentListTmstp.get(j);
		placemark.createAndSetTimeStamp().setWhen(dt.toString());

		// IconStyle
		IconStyle ic= placemark.createAndAddStyle().createAndSetIconStyle();
		ic.setHeading(1);
		ic.setScale(0.5);
		ic.setColor(couleur);
		Vec2 vc=new Vec2();
		vc.setX(0);
		vc.setY(0.5);
		vc.setXunits(Units.FRACTION);
		vc.setYunits(Units.FRACTION);
		ic.setHotSpot(vc);

		//Icon
		Icon icone=ic.createAndSetIcon();
		icone.setRefreshInterval(0.5);
		icone.setViewRefreshTime(0.5);
		icone.setHref(link);

		document.addToFeature(placemark);


		a++;


	}


	/*public static String generateColor(){
		int red = (int) (( Math.random()*255)+1);
		int green = (int) (( Math.random()*255)+1);
		int blue = (int) (( Math.random()*255)+1);
		Color RandomC = new Color(red,green,blue);
		int RandomRGB = (RandomC.getRGB());
		String RandomRGB2Hex = Integer.toHexString(RandomRGB);
		return RandomRGB2Hex;
	}*/




}
