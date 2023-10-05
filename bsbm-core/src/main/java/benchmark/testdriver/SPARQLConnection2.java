package benchmark.testdriver;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.QueryCancelledException;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdfconnection.SparqlQueryConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;

import benchmark.qualification.QueryResult;

public class SPARQLConnection2 implements ServerConnection {

    private static final Logger logger = LoggerFactory.getLogger(SPARQLConnection2.class);

    protected SparqlQueryConnection conn;


    public SPARQLConnection2(SparqlQueryConnection qef) {
        this.conn = qef;
    }

    public static long consume(QueryExecution qe) {
           org.apache.jena.query.Query query = qe.getQuery();
        assert query != null : "QueryExecution did not tell us which query it is bound to - query was null";
        long result = consume(qe, query);
        return result;
    }

    public static long consume(QueryExecution qe, org.apache.jena.query.Query query) {
        int queryType = query.getQueryType();

        long result = consume(qe, queryType);
        return result;
    }

    public static long consume(QueryExecution qe, int queryType) {
        long result;
        switch (queryType) {
        case org.apache.jena.query.Query.QueryTypeAsk:
            qe.execAsk();
            result = 1;
            break;
        case org.apache.jena.query.Query.QueryTypeConstruct:
            Iterator<Triple> itC = qe.execConstructTriples();
            result = Iterators.size(itC);
            break;
        case org.apache.jena.query.Query.QueryTypeDescribe:
            Iterator<Triple> itD = qe.execDescribeTriples();
            result = Iterators.size(itD);
            break;
        case org.apache.jena.query.Query.QueryTypeSelect:
            ResultSet rs = qe.execSelect();
            result = ResultSetFormatter.consume(rs);
            break;
        default:
            throw new RuntimeException("Unknown query type - should not happen: queryType = " + queryType);
        }

        return result;
    }

    /*
     * Execute Query with Query Object
     */
//    public void executeQuery(int queryNum, Query query, byte queryType) {
//        executeQuery(query.getQueryString(), queryType, query.getNr(), query.getQueryMix());
//    }

    //public static boolean suppressTimeoutRetrievalExceptionMessage = false;

    public QueryResult executeQuery(String queryStr, int queryType, int queryNr) {
        QueryResult result;

        if(queryType==Query.UPDATE_TYPE) {
            throw new UnsupportedOperationException();
        }


        int numResults = -1;
        List<String> heads = Collections.emptyList();
        boolean hasTimedOut = false;
        long executionTimeInMs = -1;
        boolean sorted = queryStr.toLowerCase().contains("order by"); /// hack; use proper parsing for determining that feature

        long timeout1InMs = -1;

        System.out.println("Query: " + queryStr);
        try(QueryExecution qe = conn.query(queryStr)) {
//            try {
                timeout1InMs = qe.getTimeout1();
//            } catch(Exception e) {
//                if(!suppressTimeoutRetrievalExceptionMessage) {
//                    logger.warn("QueryExecution implementation bugged as it raised an exception upon attempting to get the timeout. Further exceptions suppressed.");
//                    suppressTimeoutRetrievalExceptionMessage = true;
//                }
//            }

            Stopwatch sw = Stopwatch.createStarted();

            if(qe.getQuery().isSelectType()) {
                ResultSet rs = qe.execSelect();
                heads = rs.getResultVars();
                numResults = ResultSetFormatter.consume(rs);
            } else {
                numResults = (int)consume(qe);
            }

            System.out.println("RESULT ITEMS: " + numResults);


            executionTimeInMs = sw.stop().elapsed(TimeUnit.MILLISECONDS);

        } catch(QueryCancelledException e) {
            // TODO Determine the proper exception type

            hasTimedOut = true;
            // FIXME We may want to consider timeout2 here as well
            double t = -1.0; //timeout1InMs / 1000.0;
            System.out.println("Query " + queryNr + ": " + t + " seconds timeout!");
            //queryMix.reportTimeOut();//inc. timeout counter
            //queryMix.setCurrent(0, t);
        }

        result = new QueryResult(queryNr, queryStr, numResults, sorted, heads, hasTimedOut, timeout1InMs, executionTimeInMs);

        return result;
    }

    /*
     * execute Query with Query String
     */
    private void executeQuery(String queryStr, byte queryType, int queryNr, QueryMix queryMix) {
        QueryResult queryResult = executeQuery(queryStr, queryType, queryNr);

        if(!queryResult.isHasTimedOut()) {
            int resultCount = queryResult.getNrResults();

            int queryMixRun = queryMix.getRun() + 1;
            double timeInSeconds = queryResult.getExecutionTimeInMs() / 1000.0;

            if(logger.isInfoEnabled() && queryMixRun > 0)
                logResultInfo(queryNr, queryMixRun, timeInSeconds,
                           queryStr, queryType,
                           resultCount);

            queryMix.setCurrent(resultCount, timeInSeconds);

        } else {
            // FIXME We may want to consider timeout2 here as well
            double t = -1.0; //timeout1InMs / 1000.0;
            System.out.println("Query " + queryNr + ": " + t + " seconds timeout!");
            queryMix.reportTimeOut();//inc. timeout counter
            queryMix.setCurrent(0, t);
        }
    }


    @Override
    public void executeQuery(Query query, byte queryType) {
        executeQuery(query.getQueryString(), queryType, query.getNr(), query.getQueryMix());
    }


    @Override
    public void executeQuery(CompiledQuery query, CompiledQueryMix queryMix) {
        String queryStr = query.getQueryString();
        byte queryType = query.getQueryType();
        int queryNr = query.getNr();

        QueryResult queryResult = executeQuery(queryStr, queryType, queryNr);
        long timeout1InMs = queryResult.getTimeout1InMs();

        if(!queryResult.isHasTimedOut()) {
            int resultCount = queryResult.getNrResults();

            int queryMixRun = queryMix.getRun() + 1;
            double timeInSeconds = queryResult.getExecutionTimeInMs() / 1000.0;

            if(logger.isDebugEnabled() && queryMixRun > 0)
                logResultInfo(queryNr, queryMixRun, timeInSeconds,
                           queryStr, queryType,
                           resultCount);

            queryMix.setCurrent(resultCount, timeInSeconds);

        } else {
            // FIXME We may want to consider timeout2 here as well
            double t = timeout1InMs / 1000.0;
            System.out.println("Query " + queryNr + ": " + t + " seconds timeout!");
            queryMix.reportTimeOut();//inc. timeout counter
            queryMix.setCurrent(0, t);
        }
    }


    /*
     * (non-Javadoc)
     * @see benchmark.testdriver.ServerConnection#executeValidation(benchmark.testdriver.Query, byte, java.lang.String[])
     * Gather information about the result a query returns.
     */
    @Override
    public QueryResult executeValidation(Query query, byte queryType) {

        String queryString = query.getQueryString();
        int queryNr = query.getNr();

        QueryResult queryResult = executeQuery(queryString, queryType, queryNr);

        if(queryType != Query.UPDATE_TYPE) {
            logResultInfo(query, "TODO obtain the result set");
        }
        else
            logResultInfo(query, "");

        if(queryResult!=null)
            queryResult.setRun(query.getQueryMix().getRun());

        return queryResult;
    }


    @Override
    public void close() {
        try {
            conn.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void logResultInfo(Query query, String queryResult) {
        StringBuffer sb = new StringBuffer();

        sb.append("\n\n\tQuery " + query.getNr() + " of run " + (query.getQueryMix().getQueryMixRuns()+1) + ":\n");
        sb.append("\n\tQuery string:\n\n");
        sb.append(query.getQueryString());
        sb.append("\n\n\tResult:\n\n");
        sb.append(queryResult);
        sb.append("\n\n__________________________________________________________________________________\n");
        logger.info(sb.toString());
    }

    private void logResultInfo(int queryNr, int queryMixRun, double timeInSeconds,
            String queryString, byte queryType,
            int resultCount) {
        StringBuffer sb = new StringBuffer(1000);
        sb.append("\n\n\tQuery " + queryNr + " of run " + queryMixRun + " has been executed ");
        sb.append("in " + String.format("%.6f",timeInSeconds) + " seconds.\n" );
        sb.append("\n\tQuery string:\n\n");
        sb.append(queryString);
        sb.append("\n\n");

        //Log results
        if(queryType==Query.DESCRIBE_TYPE)
        sb.append("\tQuery(Describe) result (" + resultCount + " Bytes): \n\n");
        else if(queryType==Query.CONSTRUCT_TYPE)
        sb.append("\tQuery(Construct) result (" + resultCount + " Bytes): \n\n");
        else
        sb.append("\tQuery results (" + resultCount + " results): \n\n");


        sb.append("\n__________________________________________________________________________________\n");
        logger.info(sb.toString());
    }

}
