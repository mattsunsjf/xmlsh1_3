package org.xmlsh.exist;


import java.util.List;

import org.xmlsh.core.InputPort;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XValue;
import org.xmlsh.exist.util.ExistCommand;
import org.xmlsh.exist.util.ExistConnection;

public class invoke extends ExistCommand {


	@Override
	public int run(List<XValue> args) throws Exception {
		
		
		
		Options opts = getOptions("d=data:,raw");
		opts.parse(args);
		args = opts.getRemainingArgs();
		

		int ret = 0;
		boolean bRaw = opts.hasOpt("raw");
		

		if( args.size() != 1 ){
			
				usage("target required");
				return 1;
		}
		
		String target = args.get(0).toString();
			
		ExistConnection conn = getConnection(opts, target);
			
		InputPort in = null;
		XValue data = opts.getOptValue("data");
		if( data != null )
			in = this.getInput(data);
		

		OutputPort out = getStdout();
		
		try {
			ret = conn.invoke( in, out, bRaw );
		} finally {
			if( in != null )
				in.release();
			out.release();
		}

        conn.close();

		
		
		
		return ret;
	}

	
	

}



//
//
//Copyright (C) 2008-2014  David A. Lee.
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
