package benchmark.testdriver.model;

import java.util.List;

import org.aksw.jenax.annotation.reprogen.DefaultIri;
import org.aksw.jenax.annotation.reprogen.IriNs;
import org.aksw.jenax.annotation.reprogen.IriType;
import org.aksw.jenax.annotation.reprogen.RdfType;

@RdfType("bsbm:BsbmResult")
@DefaultIri("#{experimentBaseIri}result")
public class BsbmResult {
	
	@IriNs("bsbm")
	@IriType
	protected String experimentBaseIri;
	
	
	@IriNs("bsbm")
	protected QueryMixStats queryMixStats;

	@IriNs("bsbm")
	protected List<QueryStats> queryStats;
	
	
	public String getExperimentBaseIri() {
		return experimentBaseIri;
	}

	public BsbmResult setExperimentBaseIri(String experimentBaseIri) {
		this.experimentBaseIri = experimentBaseIri;
		return this;
	}

	public QueryMixStats getQueryMixStats() {
		return queryMixStats;
	}
	
	public BsbmResult setQueryMixStats(QueryMixStats queryMixStats) {
		this.queryMixStats = queryMixStats;
		return this;
	}
	
	public List<QueryStats> getQueryStats() {
		return queryStats;
	}
	
	public BsbmResult setQueryStats(List<QueryStats> queryStats) {
		this.queryStats = queryStats;
		return this;
	}

	@Override
	public String toString() {
		return "BsbmResult [queryMixStats=" + queryMixStats + ", queryStats=" + queryStats + "]";
	}
}
