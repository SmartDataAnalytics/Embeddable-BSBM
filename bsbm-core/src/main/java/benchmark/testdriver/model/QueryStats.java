package benchmark.testdriver.model;

import org.aksw.jenax.annotation.reprogen.DefaultIri;
import org.aksw.jenax.annotation.reprogen.IriNs;
import org.aksw.jenax.annotation.reprogen.IriType;
import org.aksw.jenax.annotation.reprogen.RdfType;

@RdfType("bsbm:QueryStats")
@DefaultIri("#{experimentBaseIri}query-#{id}")
public class QueryStats {
	
	@IriNs("bsbm")
	protected String id;

	@IriNs("bsbm")
	@IriType
	protected String experimentBaseIri;	
	
	@IriNs("bsbm")
	protected int executeCount;

	@IriNs("bsbm")
	protected double avgQet;

	@IriNs("bsbm")
	protected double avgResults;

	@IriNs("bsbm")
	protected double geometricMean;

	@IriNs("bsbm")
	protected int numTimeouts;

	@IriNs("bsbm")
	protected double frequency; // queries per second

	@IriNs("bsbm")
	protected double minQet;

	@IriNs("bsbm")
	protected double maxQet;
	
	@IriNs("bsbm")
	protected int minResults;

	@IriNs("bsbm")
	protected int maxResults;
	
	public String getId() {
		return id;
	}
	
	public QueryStats setId(String id) {
		this.id = id;
		return this;
	}
	
	public String getExperimentBaseIri() {
		return experimentBaseIri;
	}

	public QueryStats setExperimentBaseIri(String experimentBaseIri) {
		this.experimentBaseIri = experimentBaseIri;
		return this;
	}

	public int getExecuteCount() {
		return executeCount;
	}
	
	public QueryStats setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
		return this;
	}
	
	public double getAvgQet() {
		return avgQet;
	}
	
	public QueryStats setAvgQet(double avgQet) {
		this.avgQet = avgQet;
		return this;
	}
	
	public double getAvgResults() {
		return avgResults;		
	}
	
	public QueryStats setAvgResults(double avgResults) {
		this.avgResults = avgResults;
		return this;
	}
	
	public double getGeometricMean() {
		return geometricMean;		
	}
	
	public QueryStats setGeometricMean(double geometricMean) {
		this.geometricMean = geometricMean;
		return this;
	}
	
	public int getNumTimeouts() {
		return numTimeouts;
	}
	
	public QueryStats setNumTimeouts(int numTimeouts) {
		this.numTimeouts = numTimeouts;
		return this;
	}
	
	public double getFrequency() {
		return frequency;
	}
	
	public QueryStats setFrequency(double frequency) {
		this.frequency = frequency;
		return this;
	}

	public double getMinQet() {
		return minQet;
	}

	public QueryStats setMinQet(double minQet) {
		this.minQet = minQet;
		return this;
	}

	public double getMaxQet() {
		return maxQet;
	}

	public QueryStats setMaxQet(double maxQet) {
		this.maxQet = maxQet;
		return this;
	}

	public int getMinResults() {
		return minResults;
	}

	public QueryStats setMinResults(int minResults) {
		this.minResults = minResults;
		return this;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public QueryStats setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	@Override
	public String toString() {
		return "QueryStats [id=" + id + ", executeCount=" + executeCount + ", avgQet=" + avgQet + ", avgResults="
				+ avgResults + ", geometricMean=" + geometricMean + ", numTimeouts=" + numTimeouts + ", frequency="
				+ frequency + ", minQet=" + minQet + ", maxQet=" + maxQet + ", minResults=" + minResults
				+ ", maxResults=" + maxResults + "]";
	}
}
