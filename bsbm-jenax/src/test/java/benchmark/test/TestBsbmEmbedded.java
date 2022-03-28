package benchmark.test;

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.aksw.jena_sparql_api.core.FluentQueryExecutionFactory;
import org.aksw.jenax.arq.connection.core.QueryExecutionFactory;
import org.aksw.jenax.arq.connection.core.SparqlQueryConnectionJsa;
import org.aksw.jenax.stmt.parser.query.SparqlQueryParserImpl;
import org.apache.jena.rdf.model.Model;
import org.junit.Test;

import benchmark.common.TestDriverParams;
import benchmark.generator.Generator;
import benchmark.serializer.SerializerModel;
import benchmark.testdriver.LocalSPARQLParameterPool;
import benchmark.testdriver.SPARQLConnection2;
import benchmark.testdriver.TestDriver;
import benchmark.testdriver.TestDriverUtils;

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


        boolean logQueriesToFile = false;
        PrintStream out = null;
        if(logQueriesToFile) {
            PrintStream o = out = new PrintStream(new FileOutputStream("/tmp/bsbm.sparql"));

            int i[] = {0};
            qef = FluentQueryExecutionFactory.from(qef)
                    .config()
                        .withQueryTransform(q -> {
                            o.println("# Query " + (++i[0]));
                            o.println(q);
                            o.println();
                            o.println();
                            o.println();

                            return q;
                        })
                        .withParser(SparqlQueryParserImpl.create())
                    .end().create();
        }

        TestDriver testDriver = new TestDriver();
        testDriver.processProgramParameters(new String[]{"http://example.org/foobar/sparql", "-w", "0", "-runs", "1"});
        testDriver.setParameterPool(new LocalSPARQLParameterPool(testDriverParams, testDriver.getSeed()));
        testDriver.setServer(new SPARQLConnection2(new SparqlQueryConnectionJsa(qef)));

        testDriver.init();

        Model chartModel = TestDriverUtils.runWithCharts(testDriver, "http://example.org/my-bsbm-experiment/");


//    	List<Entry<StatisticalBarChartImpl, Model>> chartSpecs = ChartTransform.transform(chartModel);
//
//    	for(Entry<StatisticalBarChartImpl, Model> chartSpec : chartSpecs) {
////            CategoryChart xChart = ChartModelConfigurerXChart.toChart(chartSpec.getValue(), chartSpec.getKey());
//
////            new SwingWrapper<CategoryChart>(xChart).displayChart();
////            System.in.read();
//    	}
//
//    	if(logQueriesToFile) {
//    		out.flush();
//    		out.close();
//    	}

    }
}
