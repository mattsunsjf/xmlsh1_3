/**
 * $Id: $
 * $Date: $
 *
 */

package org.xmlsh.commands;

import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.events.XMLEvent;

import org.xmlsh.core.InputPort;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.SerializeOpts;

public class xidentity extends XCommand {


	@Override
	public int run(List<XValue> args) throws Exception {
		
		SerializeOpts opts = getSerializeOpts();
		InputPort stdin = getStdin();
		XMLEventReader	reader = stdin.asXMLEventReader(opts);
		OutputPort stdout = getStdout();
		XMLEventWriter  writer = stdout.asXMLEventWriter(opts);
		//XdmNode node = getStdin().asXdmNode(opts);
		//StAXUtils.copy( node.getUnderlyingNode() , writer );
		
		stdout.setSystemId(stdin.getSystemId());
		XMLEvent e;
		
		while( reader.hasNext() ){
			e = (XMLEvent) reader.next();
			writer.add(e);
		}
		// writer.add(reader);
		reader.close();
		writer.close();
		return 0;
		
		
	}

}



//
//
//Copyright (C) 2008, David A. Lee.
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