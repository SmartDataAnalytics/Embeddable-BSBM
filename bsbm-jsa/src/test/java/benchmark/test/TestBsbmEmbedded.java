package benchmark.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Root;

import org.aksw.beast.chart.model.StatisticalBarChart;
import org.aksw.jena_sparql_api.core.FluentQueryExecutionFactory;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.mapper.jpa.core.SparqlEntityManagerFactory;
import org.aksw.jena_sparql_api.mapper.util.JpaUtils;
import org.aksw.jena_sparql_api.update.FluentSparqlService;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.junit.Test;

import benchmark.common.TestDriverParams;
import benchmark.generator.Generator;
import benchmark.serializer.SerializerModel;
import benchmark.testdriver.LocalSPARQLParameterPool;
import benchmark.testdriver.SPARQLConnection2;
import benchmark.testdriver.TestDriver;
import benchmark.testdriver.model.BsbmResult;
import benchmark.testdriver.model.BsbmResultUtils;

public class TestBsbmEmbedded {

    @Test
    public void testBsbmEmbedded() throws Exception {

        SerializerModel serializer = new SerializerModel();
        Generator.init(new String[] {});
        Generator.setSerializer(serializer);
        Generator.run();
        TestDriverParams testDriverParams = Generator.getTestDriverParams();
        
        Model model = serializer.getModel();

        QueryExecutionFactory qef = FluentQueryExecutionFactory.from(model).create();

        TestDriver testDriver = new TestDriver();
        testDriver.processProgramParameters(new String[]{"http://example.org/foobar/sparql", "-w", "0", "-runs", "1"});
        testDriver.setParameterPool(new LocalSPARQLParameterPool(testDriverParams, testDriver.getSeed()));
        testDriver.setServer(new SPARQLConnection2(qef));

        testDriver.init();
        BsbmResult stats = testDriver.runCore();

        Model statsModel = BsbmResultUtils.toModel(stats);
        System.out.println("Result model triples: " + statsModel.size());
        RDFDataMgr.write(System.out, statsModel, RDFFormat.TURTLE_PRETTY);

        
        EntityManager em = new SparqlEntityManagerFactory()
        		.setPrefixMapping(new PrefixMappingImpl()
        	            .setNsPrefix("schema", "http://schema.org/")
        	            .setNsPrefix("dbo", "http://dbpedia.org/ontology/")
        	            .setNsPrefix("dbr", "http://dbpedia.org/resource/")
        	            .setNsPrefix("bsbm", "http://bsbm.org/"))
        		.addScanPackageName(BsbmResult.class.getPackage().getName())
        		.setSparqlService(FluentSparqlService.from(statsModel).create())
        		.getObject();
        
        List<StatisticalBarChart> matches = JpaUtils.getResultList(em, StatisticalBarChart.class, (cb, cq) -> {
            Root<StatisticalBarChart> r = cq.from(StatisticalBarChart.class);
            cq.select(r);
        });
        
        
        for(StatisticalBarChart c : matches) {
            System.out.println("Matched: " + c);
//            
//            CategoryChart xChart = ChartModelConfigurerXChart.toChart(dataModel, c);
//            VectorGraphicsEncoder.saveVectorGraphic(xChart, "/tmp/Sample_Chart", VectorGraphicsFormat.SVG);
//          //SSystem.out.println("exp: " + Math.pow(10, Math.floor(Math.log10(0.0123))));
//             new SwingWrapper<CategoryChart>(xChart).displayChart();
        }


    }
}
