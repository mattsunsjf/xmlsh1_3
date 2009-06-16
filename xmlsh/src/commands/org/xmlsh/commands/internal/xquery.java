/**
 * $Id$
 * $Date$
 *
 */

package org.xmlsh.commands.internal;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import org.xmlsh.core.InputPort;
import org.xmlsh.core.Namespaces;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.core.Options.OptionValue;
import org.xmlsh.sh.shell.Shell;
import org.xmlsh.util.Util;



public class xquery extends XCommand {

	@Override
	public int run( List<XValue> args )
	throws Exception 
	{
		
		Options opts = new Options( "f:,i:,n,q:,v,nons,ns:+" , args );
		opts.parse();
		
		Processor  processor  = Shell.getProcessor();
		
		XQueryCompiler compiler = processor.newXQueryCompiler();
		XdmNode	context = null;
		
		
		InputPort in = null;
		if( ! opts.hasOpt("n" ) ){ // Has XML data input
			OptionValue ov = opts.getOpt("i");
			if( ov != null )
				in = getInput( ov.getValue());
			else
				in = getStdin();
			
			context = in.asXdmNode(getSerializeOpts());
			
			
		}
		
		String query = null;
		
		if( opts.hasOpt("q"))
			query = opts.getOpt("q").getValue().toString();
		
		
		XQueryExecutable expr = null;
		
		List<XValue> xvargs = opts.getRemainingArgs();
		
		OptionValue ov = opts.getOpt("f");
		if( ov != null ){
			if( query != null )
				throwInvalidArg(  "Cannot specifify both -q and -f");
			
			InputPort qin = getInput(ov.getValue());
			InputStream is = qin.asInputStream(getSerializeOpts());
			query = Util.readString(is);

			String sysid = qin.getSystemId();
			if( !Util.isBlank(sysid)){
				String uri = getAbsoluteURI(sysid);
				compiler.setBaseURI(new URI(uri));
			}
			is.close();
			qin.close();
			
			
		}

		
		if( query == null ){
			if ( xvargs.size() < 1 )
				throwInvalidArg("No query specified");
			query = xvargs.remove(0).toString(); // remove arg 0
		}
		
			
		/*
		 * Add namespaces
		 */
		

		Namespaces ns = null ;
		
		if( !opts.hasOpt("nons"))
			ns = getEnv().getNamespaces();
		if( opts.hasOpt("ns")){
			Namespaces ns2 = new Namespaces();
			if( ns != null )
				ns2.putAll(ns);
			
			// Add custom name spaces
			for( XValue v : opts.getOpt("ns").getValues() )
				ns2.declare(v);
				
			
			ns = ns2;
		}
		
		
		if( ns != null ){
			for( String prefix : ns.keySet() ){
				String uri = ns.get(prefix);
				compiler.declareNamespace(prefix, uri);
				
			}
			
		}
	
		
		expr = compiler.compile( query );
		
		
		XQueryEvaluator eval = expr.load();
		if( context != null )
			eval.setContextItem(context);
		
		
		if( opts.hasOpt("v")){
			// Read pairs from args to set
			for( int i = 0 ; i < xvargs.size()/2 ; i++ ){
				String name = xvargs.get(i*2).toString();
				XValue value = xvargs.get(i*2+1);
				
				eval.setExternalVariable( new QName(name),  value.asXdmValue() );	
					
				
			}
				
			
		}
			

		
			
//	eval.run(getStdout().asDestination(getSerializeOpts()));

		OutputPort stdout = getStdout();
		Destination ser = stdout.asDestination(getSerializeOpts());
		boolean bFirst = true ;
		boolean bAnyOut = false ;
		for( XdmItem item : eval ){
			bAnyOut = true ;
			if( ! bFirst )
				stdout.writeSequenceSeperator(); // Thrashes variable output !
			bFirst = false ;
			//processor.writeXdmValue(item, ser );
			Util.writeXdmValue(item, ser);

			
		}
		if( bAnyOut )
			stdout.writeSequenceTerminator(); // write "\n"
		
		if( in != null)
			in.close();
		
		return 0;


	}


	

}

//
//
//Copyright (C) 2008,2009 , David A. Lee.
//
//The contents of this file are subject to the "Simplified BSD License" (the "License");
//you may not use this file except in compliance with the License. You may obtain a copy of the
//License at http://www.opensource.org/licenses/bsd-license.php 
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied.
//See the License for the specific language governing rights and limitations under the License.
//
//The Original Code is: all this file.
//
//The Initial Developer of the Original Code is David A. Lee
//
//Portions created by (your name) are Copyright (C) (your legal entity). All Rights Reserved.
//
//Contributor(s): none.
//