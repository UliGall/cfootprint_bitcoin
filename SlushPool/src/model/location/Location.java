package model.location;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 
 * Starting class for the location json. Contains a data object (LocationData) which again holds data objects.
 * In adition to that, this class has a own toCSV() function, parsing its own content into a string.
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class Location {
	
	private String checksum;
	private LocationData data;
	private String error_message;
	private boolean modified;
	private int status_code;
	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public LocationData getData() {
		return data;
	}

	public void setData(LocationData data) {
		this.data = data;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public Location() {
		
	}
	
	
	/**
	 * Creating a csv from this object. The columns are named after the timestamps of the data objects.
	 * @return this object as a valid csv string
	 */
	public String toCSV() {
		StringBuffer csv = new StringBuffer();
		LocationData data = this.getData();
		csv.append(";");
		for(String timestamp : data.getTimestamp()) {
			csv.append(timestamp + ";");
		}
		csv.append("\n");
		for (Field field : data.getData().getClass().getDeclaredFields()) {
			field.setAccessible(true);
			csv.append(field.getName() + ";");
			
			try {
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) field.get(data.getData());
				for (String s : list) {
					csv.append(s + ";");
				}
				csv.append("\n");
			} catch (NullPointerException e) {
				continue;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			
		}
		return csv.toString();
	}

	
	
	
}
