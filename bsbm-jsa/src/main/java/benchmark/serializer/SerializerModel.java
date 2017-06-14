package benchmark.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public class SerializerModel
    extends NTriples
{
    protected Model model;

    public SerializerModel() {
        this(false);
    }

    public SerializerModel(boolean forwardChaining) {
        super(new StringWriter(), forwardChaining);
        model = ModelFactory.createDefaultModel();
    }

    public Model getModel() {
        return model;
    }

    @Override
    public void serialize() {
        for(Writer writer : fileWriter) {
            try {
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String str = writer.toString();
            RDFDataMgr.read(model, new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), Lang.NTRIPLES);
        }

        super.serialize();
    }

}
