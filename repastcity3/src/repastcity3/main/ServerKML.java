package repastcity3.main;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.RefreshMode;
import de.micromata.opengis.kml.v_2_2_0.Units;
import de.micromata.opengis.kml.v_2_2_0.Vec2;
import repastcity3.agent.Student;

public class ServerKML {
	static HashMap<String,ArrayList<List<Coordinate>>> StudentPath = new HashMap<String, ArrayList<List<Coordinate>>>();
	static HashMap<String,ArrayList<List<Instant>>> StudentTimeStamp = new HashMap<String, ArrayList<List<Instant>>>();
	static List<com.vividsolutions.jts.geom.Coordinate> current;
	static List<Instant> currentListTmstp;





	public static void main(String[] args) throws IOException, ClassNotFoundException {

		ServerSocket servSocket;
		ServerSocket servSocket1;
		Socket fromClientSocket;
		Socket fromClientSocket1;
		int cTosPortNumber = 1777;
		int cTosPortNumber1 = 1779;
		Student stud=new Student();
		ArrayList<List<Coordinate>> st;
		de.micromata.opengis.kml.v_2_2_0.Kml kml = de.micromata.opengis.kml.v_2_2_0.KmlFactory.createKml();



		//add the link 
		//de.micromata.opengis.kml.v_2_2_0.Link lnk=kml.createAndSetNetworkLink().createAndSetLink();
		de.micromata.opengis.kml.v_2_2_0.Link lnk = new de.micromata.opengis.kml.v_2_2_0.Link();
		/*lnk.setHref("F:/ESIROI/Stage/Projet/CitySystemsAndMobility/repastcity3/map.kml");
		lnk.setRefreshMode(RefreshMode.ON_INTERVAL);
		lnk.setRefreshInterval(10);*/

		/*lnk.setViewRefreshMode(ViewRefreshMode.ON_REGION);
		lnk.setViewRefreshTime(1);*/


		//Create the document tag
		de.micromata.opengis.kml.v_2_2_0.Document document = kml.createAndSetDocument().withName("MyMarkers");



		servSocket = new ServerSocket(cTosPortNumber);
		servSocket1 = new ServerSocket(cTosPortNumber1);
		System.out.println("Waiting for a connection on " + cTosPortNumber);

		fromClientSocket = servSocket.accept();
		fromClientSocket1 = servSocket1.accept();

		ObjectOutputStream oos = new ObjectOutputStream(fromClientSocket.getOutputStream());

		ObjectInputStream ois = new ObjectInputStream(fromClientSocket.getInputStream());

		DataOutputStream dos = new DataOutputStream(fromClientSocket1.getOutputStream());

		DataInputStream dis = new DataInputStream(fromClientSocket1.getInputStream());

		int compteurStudent = dis.readInt();
		System.out.println("Le nombre d'agents est: "+compteurStudent);
		
		int x=0;
		
		ArrayList<Student> studentList=new ArrayList<Student>();
		for (int y=0;y<compteurStudent;y++){
			studentList.add(new Student());
		}
		System.out.println("List size= "+studentList.size());

		while (true) {

			System.out.println(ois.readUnshared());
				stud = (Student) ois.readUnshared();
				System.out.println(stud.toString());
				System.out.println(stud.getagentPath());
				System.out.println(stud.getAllTimeStamp());
				
				studentList.set(x, stud);
		
			

				System.out.println("SIZE:::::: "+studentList.size());

			//if(studentList.size()==compteurStudent){
				for(int l=0;l<studentList.size();l++)
				{

					Student s=studentList.get(l);
					String couleurStudent=s.getColor();

					for(int i = 0; i < s.getagentPath().size(); i++) {
						current=s.getagentPath().get(i);
						
						currentListTmstp=s.getAllTimeStamp().get(i);


						for(int j = 0; j < current.size(); j=j+30){

							export("http://maps.google.com/mapfiles/ms/icons/cabs.png",current, currentListTmstp, s, document, couleurStudent, j, 30);
						}
					}
				}

				//kml.marshal(new File("Simulation.kml"));
				System.out.println("==========================Export done=====================");
				x++;
				if(x==studentList.size()){
					x=0;
				}
				

			}
		//}




		//oos.writeObject("bye bye");
		


		//oos.close();
		//dos.close();

		//fromClientSocket.close();

	}




	public static void export(String link,List<com.vividsolutions.jts.geom.Coordinate> current,List<Instant> currentListTmstp, Student s, de.micromata.opengis.kml.v_2_2_0.Document document, String couleur, int j, int a){


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


		java.time.Instant dt=currentListTmstp.get(j);
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

}
