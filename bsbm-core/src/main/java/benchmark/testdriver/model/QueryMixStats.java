package benchmark.testdriver.model;

import org.aksw.jena_sparql_api.mapper.annotation.DefaultIri;
import org.aksw.jena_sparql_api.mapper.annotation.IriNs;
import org.aksw.jena_sparql_api.mapper.annotation.RdfType;

@RdfType("bsbm:QueryMixStats")
@DefaultIri("bsbm:bar")
public class QueryMixStats {

	@IriNs("bsbm")
	protected Integer scaleFactor;
	
	@IriNs("bsbm")
	protected int warmups;
	
	@IriNs("bsbm")
	protected int nrThreads;
	
	@IriNs("bsbm")
	protected long seed;
	
	@IriNs("bsbm")
	protected int queryMixRuns;
	
	@IriNs("bsbm")
	protected double minQueryMixRuntime;
	
	@IriNs("bsbm")
	protected double maxQueryMixRuntime;
	
	@IriNs("bsbm")
	protected double totalRuntime;
	
	// Related to multi threading
	@IriNs("bsbm")
	protected double actualTotalRuntime;
	
	@IriNs("bsbm")
	protected double qmph;

	@IriNs("bsbm")
	protected double cqet;
	
	@IriNs("bsbm")
	protected double queryMixGeometryMean;
	
	public Integer getScaleFactor() {
		return scaleFactor;
	}

	public QueryMixStats setScaleFactor(Integer scaleFactor) {
		this.scaleFactor = scaleFactor;
		return this;
	}

	public int getWarmups() {
		return warmups;
	}

	public QueryMixStats setWarmups(int warmups) {
		this.warmups = warmups;
		return this;
	}

	public int getNrThreads() {
		return nrThreads;
	}

	public QueryMixStats setNrThreads(int nrThreads) {
		this.nrThreads = nrThreads;
		return this;
	}

	public int getQueryMixRuns() {
		return queryMixRuns;
	}

	public QueryMixStats setQueryMixRuns(int queryMixRuns) {
		this.queryMixRuns = queryMixRuns;
		return this;
	}

	public long getSeed() {
		return seed;
	}

	public QueryMixStats setSeed(long seed) {
		this.seed = seed;
		return this;
	}

	public double getMinQueryMixRuntime() {
		return minQueryMixRuntime;
	}

	public QueryMixStats setMinQueryMixRuntime(double minQueryMixRuntime) {
		this.minQueryMixRuntime = minQueryMixRuntime;
		return this;
	}

	public double getMaxQueryMixRuntime() {
		return maxQueryMixRuntime;
	}

	public QueryMixStats setMaxQueryMixRuntime(double maxQueryMixRuntime) {
		this.maxQueryMixRuntime = maxQueryMixRuntime;
		return this;
	}

	public double getTotalRuntime() {
		return totalRuntime;
	}

	public QueryMixStats setTotalRuntime(double totalRuntime) {
		this.totalRuntime = totalRuntime;		
		return this;
	}

	public double getQmph() {
		return qmph;
	}

	public QueryMixStats setQmph(double qmph) {
		this.qmph = qmph;
		return this;
	}

	public double getActualTotalRuntime() {
		return actualTotalRuntime;
	}

	public QueryMixStats setActualTotalRuntime(double actualTotalRuntime) {
		this.actualTotalRuntime = actualTotalRuntime;
		return this;
	}

	public double getCqet() {
		return cqet;
	}

	public QueryMixStats setCqet(double cqet) {
		this.cqet = cqet;
		return this;
	}

	public double getQueryMixGeometryMean() {
		return queryMixGeometryMean;
	}

	public QueryMixStats setQueryMixGeometryMean(double queryMixGeometryMean) {
		this.queryMixGeometryMean = queryMixGeometryMean;
		return this;
	}

	@Override
	public String toString() {
		return "QueryMixStats [scaleFactor=" + scaleFactor + ", warmups=" + warmups + ", nrThreads=" + nrThreads
				+ ", seed=" + seed + ", queryMixRuns=" + queryMixRuns + ", minQueryMixRuntime=" + minQueryMixRuntime
				+ ", maxQueryMixRuntime=" + maxQueryMixRuntime + ", totalRuntime=" + totalRuntime
				+ ", actualTotalRuntime=" + actualTotalRuntime + ", qmph=" + qmph + ", cqet=" + cqet
				+ ", queryMixGeometryMean=" + queryMixGeometryMean + "]";
	}	
}
