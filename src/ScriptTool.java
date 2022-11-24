import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Hashtable;


/**
 * @author Josh, Nick
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScriptTool {

	private OOPS anOOPS;
	private Hashtable theToolButtonList;
	
	public ScriptTool(OOPS anOOPS) {
		this.anOOPS = anOOPS;
		theToolButtonList = new Hashtable();
		Component [] theToolButtonArray = anOOPS.getToolPanel().getComponents();
		for (int i=0;i < theToolButtonArray.length;i++) {
			if ( theToolButtonArray[i] instanceof OOPSButton) {
	    		OOPSButton ob = (OOPSButton) theToolButtonArray[i];
	    		theToolButtonList.put(ob.getToolTipText(), ob);
	    	}
	    }
	}
	
	public Tool getTool(String aTool) {
		OOPSButton oButton = (OOPSButton) theToolButtonList.get(aTool);
		oButton.doClick();
	    return anOOPS.getCurrentTool();
	}
}

