package com.ma.volvo;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public class InteriorServiceImpl implements InteriorService{

	private XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
	private InteriorResponse interiorResponse = new InteriorResponse();
	private DatabaseManager databaseManager = new DatabaseManager();
	private long uniqeIndexErrorCode = 23000l; 	
	
	@WebMethod
	public ArrayList<String> getDataByPno12(String pno12){
		ArrayList<String> data = null;
		try {
            data = databaseManager.getDataByPno12(pno12);
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
		return data;
	}
	
	@WebMethod
	public void UnmarshallXml() {
		xmlUnmarshaller.UnmarshalXml();
		interiorResponse = xmlUnmarshaller.getInteriorResponse();
		insertIntoDB();
	}
	
    private void insertIntoDB() {
    	try {
            long retVal = databaseManager.insertData(interiorResponse);
            if(retVal == uniqeIndexErrorCode || retVal == -1 ) {
            	System.out.println("This row already exit in the table. Handle error");
            	return;
            }
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
    }
}
