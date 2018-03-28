package backend;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.List;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import java.io.FileWriter;
import java.io.IOException;

public class backend {

	public static void main(String[] args) {

		String path = "/home/user/workspace/backend/src/test.json"; // data source file path

		// ###################### Creating example query object #############################
	
		JSONObject jd = new JSONObject();
		jd.put("location", "Moscow");
		jd.put("guests", "1");
		jd.put("datein", "2018-10-10");
		jd.put("dateout", "2018-10-12");

		// ####################################################################################

		System.out.println(search(path, jd));

	}

	public static JSONObject search(String path, JSONObject jd) {
		JSONObject returnObj = new JSONObject();

		for (Object obj : read(path)) { // visit all object in file
			JSONObject loc = (JSONObject) obj;
			String loc_location = (String) loc.get("location");
			int loc_guests = Integer.parseInt((String) loc.get("guests"));

			String jd_location = (String) jd.get("location");
			int jd_guests = Integer.parseInt((String) jd.get("guests"));

			if (loc_location.equalsIgnoreCase(jd_location) && (jd_guests <= loc_guests)) { 

				JSONArray loc_avaliable_date = (JSONArray) loc.get("avaliable_date");
				String jd_datein = (String) jd.get("datein");
				String jd_dateout = (String) jd.get("dateout");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date datein=null;
				Date dateout=null;
				try {
					datein= sdf.parse(jd_datein);
					dateout = sdf.parse(jd_dateout);
				} catch (java.text.ParseException e1) {
					e1.printStackTrace();
				}
				
				ArrayList<Date> datesBetweenInOut = getDatesBetweenTwoDates(datein, dateout);

				ArrayList<Date> availDatesInFile = new ArrayList<Date>();

				for (Object o : loc_avaliable_date) { // fill available date in a Arraylist in json file														

					try {
						availDatesInFile.add(sdf.parse(o.toString()));
					} catch (java.text.ParseException e) {
						e.printStackTrace();
					}
				}
				
				
				if(availDatesInFile.containsAll(datesBetweenInOut)){
			
					loc.remove("avaliable_date");
					JSONArray kk = new JSONArray();
					
					for (Date date : datesBetweenInOut) {
						
						kk.add(sdf.format(date));							
					}					
				
					loc.put("avaliable_date", kk);
					returnObj=loc;
				}				
			}

		}

		return returnObj;

	}

	public static JSONArray read(String path) {// takes json file path and
												// returns JSONArray

		JSONParser parser = new JSONParser();
		JSONArray arr = null;
		try {

			arr = (JSONArray) parser.parse(new FileReader(path));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return arr;
	}

	public static void write(JSONObject obj, String path) {// takes json obj and
															// write into file
															// in path

		try (FileWriter file = new FileWriter(path)) {

			file.write(obj.toJSONString());
			file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Date> getDatesBetweenTwoDates(Date startDate, Date endDate) {
		ArrayList<Date> datesInRange = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(startDate);

		Calendar endCalendar = new GregorianCalendar();
		endCalendar.setTime(endDate);

		while (calendar.before(endCalendar)) {
			Date result = calendar.getTime();
			datesInRange.add(result);
			calendar.add(Calendar.DATE, 1);
		}
		return datesInRange;
	}
}

/*
 * System.out.println(jsonObject);
 * 
 * String name = (String) jsonObject.get("name"); System.out.println(name);
 * 
 * long age = (Long) jsonObject.get("age"); System.out.println(age);
 * 
 * // loop array JSONArray msg = (JSONArray) jsonObject.get("messages");
 * Iterator<String> iterator = msg.iterator(); while (iterator.hasNext()) {
 * System.out.println(iterator.next()); }
 */
