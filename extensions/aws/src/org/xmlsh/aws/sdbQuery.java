package org.xmlsh.aws;

import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import net.sf.saxon.s9api.SaxonApiException;

import org.xmlsh.aws.util.AWSSDBCommand;
import org.xmlsh.core.CoreException;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.UnexpectedException;
import org.xmlsh.core.XValue;

import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;


public class sdbQuery	 extends  AWSSDBCommand {


    /**
     * @param args
     * @throws IOException 
     */
    @Override
    public int run(List<XValue> args) throws Exception {

        Options opts = getOptions("c=consistant");
        parseOptions(opts, args);

        args = opts.getRemainingArgs();
        setSerializeOpts(this.getSerializeOpts(opts));


        boolean bConsistant = opts.hasOpt("consistant");


        try {
            getSDBClient(opts);
        } catch (UnexpectedException e) {
            usage( e.getLocalizedMessage() );
            return 1;

        }

        if( args.size() !=1 ){
            usage(getName()+ ":" + "select ...");

        }
        String select = args.remove(0).toString();


        int ret = -1;
        ret = query(bConsistant,select);



        return ret;


    }


    private int query(boolean bConsistant, String select) throws IOException, XMLStreamException, SaxonApiException, CoreException 
    {

        OutputPort stdout = getStdout();
        mWriter = stdout.asXMLStreamWriter(getSerializeOpts());





        startDocument();
        startElement(getName());





        SelectRequest selectRequest = new SelectRequest(select,bConsistant);

        String token = null ;
        do {

            traceCall("select");

            SelectResult result = mAmazon.select(selectRequest).withNextToken(token);
            for( Item item :result.getItems())
                writeItem(item);
            token = result.getNextToken();

        } while( token != null );

        endElement();
        endDocument();








        closeWriter();
        stdout.writeSequenceTerminator(getSerializeOpts());
        stdout.release();
        return 0;


    }


    private void writeItem(Item item) throws XMLStreamException {
        startElement("item");
        attribute("name" , item.getName());
        writeAttributes( item.getAttributes());
        endElement();

    }


    @Override
    public void usage() {
        super.usage();
    }





}
