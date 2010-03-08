/**
 * $Id: xfile.java 356 2010-01-01 15:46:31Z daldei $
 * $Date: 2010-01-01 10:46:31 -0500 (Fri, 01 Jan 2010) $
 *
 */

package org.xmlsh.commands.internal;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLStreamWriter;

import org.xmlsh.commands.util.Checksum;
import org.xmlsh.core.InputPort;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.SerializeOpts;


public class xmd5sum extends XCommand
{
	private static String sDocRoot = "xmd5";
	
	
	public int run(  List<XValue> args  )	throws Exception
	{
		Options opts = new Options("b=binary,x=xml",SerializeOpts.getOptionDefs());
		opts.parse(args);
		args = opts.getRemainingArgs();

		XMLStreamWriter 	out = null ;
		OutputPort stdout = mShell.getEnv().getStdout();	

		SerializeOpts serializeOpts = getSerializeOpts(opts);
		
		
		out = stdout.asXMLStreamWriter(serializeOpts);
		out.writeStartDocument();
		out.writeStartElement(sDocRoot);
			
		
		final  String sFile = "file";
		final	String sName = "name";
		final 	String sMd5 = "md5";
		final   String sLen = "length";

		
		
		
		
		for( XValue arg : args ){
	
			InputPort inp = null;
			InputStream in;
			try {
				inp =  getInput(arg);
				in = inp.asInputStream(serializeOpts);
			} catch (Exception e) {
				printErr("Error reading uri: " + arg.toString());
				continue;
			}
			
			
			Checksum cs = Checksum.calcChecksum(in);
			in.close();
			
			out.writeStartElement(sFile);
			out.writeAttribute(sName, inp.getSystemId());
			out.writeAttribute(sMd5, cs.getMD5());
			out.writeAttribute(sLen , String.valueOf( cs.getLength()) );
			out.writeEndElement();
			inp.close();
			
		}
		out.writeEndElement();
		out.writeEndDocument();
		
		stdout.writeSequenceTerminator(serializeOpts);
				
		return 0;
		
		
	}


	
}

//
//
//Copyright (C) 2008,2009,2010 , David A. Lee.
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