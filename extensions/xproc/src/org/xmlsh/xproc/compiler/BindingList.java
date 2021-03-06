/**
 * $Id: $
 * $Date: $
 *
 */

package org.xmlsh.xproc.compiler;

import java.util.ArrayList;

import org.xmlsh.xproc.util.XProcException;

@SuppressWarnings("serial")
class BindingList extends ArrayList<Binding> {

	/*
	 * Serialize a binding list
	 * return TRUE if there were any bindings
	 */
	boolean serialize(OutputContext c) throws XProcException {
		
		for( Binding binding : this )
			binding.serialize(c);
				
		return ! this.isEmpty();
			
			
		
	}
	
	boolean hasInputs() {
		for( Binding binding : this )
			if( binding.isInput() )
				return true ;
		return false ;
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
