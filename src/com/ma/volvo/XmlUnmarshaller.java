package com.ma.volvo;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlUnmarshaller {

    private File file = new File("minxml2.xml");
    private JAXBContext jaxbContext;
	private  InteriorResponse interiorResponse = null;
	private long uniqeIndexErrorCode = 23000l; 

    
    public XmlUnmarshaller() {

    }
    
    public void UnmarshalXml() {
        try {
            jaxbContext = JAXBContext.newInstance(InteriorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            interiorResponse = (InteriorResponse) unmarshaller.unmarshal(file);
            List<InteriorRoom> cuList = interiorResponse.getCuList();
            if(!cuList.isEmpty()) {
            	for (InteriorRoom colUph : cuList) {
            		interiorResponse.addColorUpholstery(colUph.getColor(), colUph.getUpholstery());
            	}
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }    	
    }
    
    public InteriorResponse getInteriorResponse() {
    	return interiorResponse;
    }
}
