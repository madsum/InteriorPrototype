package com.ma.volvo;

import java.util.ArrayList;

public class TestApp {
	
    private static XmlUnmarshaller xmlUnmarshaller = new XmlUnmarshaller();
	private static InteriorResponse interiorResponse;
    private static DatabaseManager databaseManager = new DatabaseManager();
	static long uniqeIndexErrorCode = 23000l; 
    static ArrayList<String> data = null;;
    static String pno12 = "ABCDEEX";
    static long str_week_from = 202017;
    static long str_week_to = 202035;
    static String color = "100";
    static String upholstery = "RA0X";

	public static void main(String[] args) {
        // xmlUnmarshaller = new XmlUnmarshaller();
		xmlUnmarshaller.UnmarshalXml();
        interiorResponse = xmlUnmarshaller.getInteriorResponse();
        // insertIntoDB();
        data = getDataByPno12(pno12);
        // data = getDataByAll(pno12, str_week_from, str_week_to, color, upholstery);
	}
	
    static ArrayList<String> getDataByPno12(String pno12) {
        try {
            data = databaseManager.getDataByPno12(pno12);
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
        return data;
	}
	
    static ArrayList<String> getDataByAll(String pno12, long str_week_from, long str_week_to, String color, String upholstery) {
        try {
            data = databaseManager.getDataByAll(pno12, str_week_from, str_week_to, color, upholstery);
        } catch (Exception e) {
            System.out.println("Error when insert data check it. Handle error");
        }
        return data;
    }

    static void insertIntoDB() {
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
