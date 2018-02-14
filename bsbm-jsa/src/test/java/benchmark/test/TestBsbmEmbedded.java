package benchmark.test;

import java.util.List;
import java.util.Map.Entry;

import org.aksw.beast.chart.ChartTransform;
import org.aksw.beast.chart.model.StatisticalBarChart;
import org.aksw.beast.viz.xchart.ChartModelConfigurerXChart;
import org.aksw.jena_sparql_api.core.FluentQueryExecutionFactory;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;
import org.junit.Test;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.SwingWrapper;

import benchmark.common.TestDriverParams;
import benchmark.generator.Generator;
import benchmark.serializer.SerializerModel;
import benchmark.testdriver.BsbmResultUtils;
import benchmark.testdriver.LocalSPARQLParameterPool;
import benchmark.testdriver.SPARQLConnection2;
import benchmark.testdriver.TestDriver;
import benchmark.testdriver.model.BsbmResult;

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
        BsbmResult stats = testDriver.runCore("http://example.org/my-bsbm-experiment/");

        Model statsModel = BsbmResultUtils.toModel(stats);
        System.out.println("Result model triples: " + statsModel.size());
        RDFDataMgr.write(System.out, statsModel, RDFFormat.TURTLE_PRETTY);


        Model chartModel = RDFDataMgr.loadModel("bsbm-ldchart-config.ttl");
        chartModel.add(statsModel);

    	List<Entry<StatisticalBarChart, Model>> chartSpecs = ChartTransform.transform(chartModel);
    	
    	for(Entry<StatisticalBarChart, Model> chartSpec : chartSpecs) {
            CategoryChart xChart = ChartModelConfigurerXChart.toChart(chartSpec.getValue(), chartSpec.getKey());

            new SwingWrapper<CategoryChart>(xChart).displayChart();
            System.in.read();
    	}
    }
}
