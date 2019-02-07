package model.location;

import java.util.List;

/**
 * 
 * Container Class holding a list of timestamps and the actually data.
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class LocationData {
	private Data data;
	private List<String> timestamp;
	
	public LocationData() {
		
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public List<String> getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(List<String> timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
