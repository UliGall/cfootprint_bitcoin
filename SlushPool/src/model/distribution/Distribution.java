package model.distribution;

/**
 * 
 * Starting class for the distribution json. Contains a data object (DistributionData) which again holds the buckets.
 * In adition to that, this class has a own toCSV() function, parsing its own content into a string.
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class Distribution {
	private String checksum;
	private DistributionData data;
	private String error_message;
	private boolean modified;
	private int status_code;
	
	public Distribution() {
		
	}
	
	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
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

	public DistributionData getData() {
		return data;
	}

	public void setData(DistributionData data) {
		this.data = data;
	}
	
	/**
	 * Converting this object into a valid csv string. It takes Index, Bucket, Max Ghps, Min Ghps, Selected, Total Gehps and User count into account.
	 * @return object data as csv
	 */
	public String toCSV() {
		StringBuffer csv = new StringBuffer();
		csv.append("Index;Bucket;Max Ghps;Min Ghps;Selected;Total Ghps;User Count;\n");
		DistributionData data = this.getData();
		for (int i = 0; i < data.getBuckets().size(); i++) {
			Bucket bucket = data.getBuckets().get(i);
			csv.append(i + ";" + bucket.getBucket() + ";" + bucket.getMax_ghps() + ";" + bucket.getMin_ghps() + ";");
			csv.append(bucket.isSelected() + ";" + bucket.getTotal_ghps() + ";" + bucket.getUser_count() + ";\n");
		}
		return csv.toString();
	}
}
