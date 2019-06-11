package benchmark.testdriver;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import benchmark.testdriver.model.BsbmResult;

public class TestDriverUtils {
	
	public static Model runWithCharts(TestDriver testDriver, String experimentStr) {
        Resource experiment = ResourceFactory.createResource(experimentStr);
        BsbmResult stats = testDriver.runCore(experiment.getURI());

        Model statsModel = BsbmResultUtils.toModel(stats);
        //System.out.println("Result model triples: " + statsModel.size());
        RDFDataMgr.write(System.out, statsModel, RDFFormat.TURTLE_PRETTY);


        Model chartModel = RDFDataMgr.loadModel("bsbm-ldchart-config.ttl");
        
        // Configure the chart for the current experiment
//        chartModel
//        	.listSubjectsWithProperty(RDF.type, RDF.type)        	
//        	.forEachRemaining(r -> r
//        			.addProperty(CV.sliceProperty, IV.experiment)
//        			.addProperty(IV.experiment, experiment));
                
        chartModel.add(statsModel);
        
        return chartModel;
	}
}
