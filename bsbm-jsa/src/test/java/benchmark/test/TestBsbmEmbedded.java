package benchmark.test;

import org.aksw.jena_sparql_api.core.FluentQueryExecutionFactory;
import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.apache.jena.rdf.model.Model;
import org.junit.Test;

import benchmark.common.TestDriverParams;
import benchmark.generator.Generator;
import benchmark.serializer.SerializerModel;
import benchmark.testdriver.LocalSPARQLParameterPool;
import benchmark.testdriver.SPARQLConnection2;
import benchmark.testdriver.TestDriver;

public class TestBsbmEmbedded {

    @Test
    public void testBsbmEmbedded() {

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
        testDriver.run();

    }
}
