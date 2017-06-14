# BSBM-Fork
This project is a fork of the [BSBM Source code (revision 81)](https://sourceforge.net/p/bsbmtools/code/81) intended for closer integration in projects.




## Changes over the original code base

* Converted to a maven project together with deployment/release of artifacts
* Replaced the original logger with slf4j
* Enhanced code base to not require files for communication between components - this means:
  * Added a TestDriverParams class. The Generator yields objects of this class, and the TestDriver can be configured with such an instance.
  * Added a SerializerModel class which writes the generated dataset to a Jena model (instead of a file)

## Maven artifacts

```xml
<dependency>
    <groupId>org.aksw.bsbm</groupId>
    <artifactId>bsbm-jsa</artifactId>
    <version>3.1</version>
</dependency>
```

For snapshot version, add our archiva:
```xml
    <repositories>
        <repository>
            <id>maven.aksw.snapshots</id>
            <name>AKSW Snapshot Repository</name>
            <url>http://maven.aksw.org/archiva/repository/snapshots</url>
            <releases>
                <enabled>false</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
```


For the original bsbm without the jena-sparql-api (jsa) integration, use `bsbm-core` instead.

## Usage

```java
    // The BSBM Generator is a static class 
    Serializer serializer = new SerializerModel();
    Generator.init(Arrays.<String>asList());
    Generator.setSerializer(serializer);
    Generator.run();
    
    // Obtain the result artifacts
    TestDriverParams testDriverParams = Generator.getTestDriverParams();
    Model model = serializer.getModel();

    // Create the jena-sparql-api query abstraction
    QueryExecutionFactory qef = FluentQueryExecutionFactory.http("http://your.endpoint/sparql", "http://your-bsbm-dataset-graph.org").create();

    // Configure and run the test driver
    TestDriver testDriver = new TestDriver(Arrays.asList<String>());
    testDriver.setParameterPool(new LocalSPARQLParameterPool(testDriverParams, testDriver.getSeed()));
    testDriver.setServer(new SPARQLConnection2(qef));

    testDriver.run();
```

## TODOs

* Extend the APIs to make benchmark results programmatically accessible
* Compare benchmark runs of the forked code base to those of original one to ensure we did not introduce any mistakes that influence the benchmark results

