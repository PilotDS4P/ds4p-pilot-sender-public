/**
 * This software is being provided per FARS 52.227-14 Rights in Data - General.
 * Any redistribution or request for copyright requires written consent by the
 * Department of Veterans Affairs.
 */
/*
 * AESValidator.java
 *
 * Created on April 12, 2012, 2:21 PM
 */
 
package gov.va.ehtac.meaningfuluse;           

import com.vaadin.Application;
import com.vaadin.ui.*;
import com.vaadin.data.*;
import gov.va.ehtac.meaningfuluse.panels.MainPanel;
/** 
 *
 * @author Duane DeCouteau
 * @version 
 */

public class AESValidator extends Application {

    @Override
    public void init() {
        setTheme("runo");
	Window mainWindow = new Window("Data Segmentation For Privacy ST");
        MainPanel p = new MainPanel(this);
	mainWindow.addComponent(p);
	setMainWindow(mainWindow);
    }

}
