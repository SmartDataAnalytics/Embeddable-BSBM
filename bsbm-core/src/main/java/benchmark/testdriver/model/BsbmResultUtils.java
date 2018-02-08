package benchmark.testdriver.model;

import javax.persistence.EntityManager;

import org.aksw.jena_sparql_api.mapper.jpa.core.SparqlEntityManagerFactory;
import org.aksw.jena_sparql_api.update.FluentSparqlService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.impl.PrefixMappingImpl;

public class BsbmResultUtils {
	public static Model toModel(BsbmResult stats) {
		Model result;
		try {
			result = ModelFactory.createDefaultModel();
	        EntityManager em = new SparqlEntityManagerFactory()
	        		.setPrefixMapping(new PrefixMappingImpl()
	        	            .setNsPrefix("schema", "http://schema.org/")
	        	            .setNsPrefix("dbo", "http://dbpedia.org/ontology/")
	        	            .setNsPrefix("dbr", "http://dbpedia.org/resource/")
	        	            .setNsPrefix("bsbm", "http://bsbm.org/"))
	        		.addScanPackageName(BsbmResult.class.getPackage().getName())
	        		.setSparqlService(FluentSparqlService.from(result).create())
	        		.getObject();
	
	        em.persist(stats);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}

        return result;
	}
}
