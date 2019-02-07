package model.distribution;

/**
 * 
 * This class is holding the actual data of each bucket. Containing the bucket number, max ghps, min ghps, selected, total_ghps and the user count.
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class Bucket {
	private int bucket;
	private double max_ghps;
	private double min_ghps;
	private boolean selected;
	private double total_ghps;
	private int user_count;
	
	public Bucket() {
		
	}

	public int getBucket() {
		return bucket;
	}

	public void setBucket(int bucket) {
		this.bucket = bucket;
	}

	public double getMax_ghps() {
		return max_ghps;
	}

	public void setMax_ghps(double max_ghps) {
		this.max_ghps = max_ghps;
	}

	public double getMin_ghps() {
		return min_ghps;
	}

	public void setMin_ghps(double min_ghps) {
		this.min_ghps = min_ghps;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public double getTotal_ghps() {
		return total_ghps;
	}

	public void setTotal_ghps(double total_ghps) {
		this.total_ghps = total_ghps;
	}

	public int getUser_count() {
		return user_count;
	}

	public void setUser_count(int user_count) {
		this.user_count = user_count;
	}
	
	
}
