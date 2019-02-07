package model.distribution;

import java.util.List;

/**
 * 
 * Container Class holding a list of buckets, max_bucket, max_users and total_ghps
 * 
 * @author Isabel Hoekstein & Christian Stoll
 *
 */
public class DistributionData {
	private List<Bucket> buckets;
	private double max_bucket;
	private double max_users;
	private double total_ghps;
	
	public DistributionData() {
		
	}

	public List<Bucket> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}

	public double getMax_bucket() {
		return max_bucket;
	}

	public void setMax_bucket(double max_bucket) {
		this.max_bucket = max_bucket;
	}

	public double getMax_users() {
		return max_users;
	}

	public void setMax_users(double max_users) {
		this.max_users = max_users;
	}

	public double getTotal_ghps() {
		return total_ghps;
	}

	public void setTotal_ghps(double total_ghps) {
		this.total_ghps = total_ghps;
	}
	
}
