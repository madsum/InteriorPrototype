package com.ma.volvo;

public class TestApp {
	
	private static XmlUnmarshaller xmlUnmarshaller;
	private static InteriorResponse interiorResponse;
	private static DatabaseManager databaseManager;
	static long uniqeIndexErrorCode = 23000l; 

	public static void main(String[] args) {
		xmlUnmarshaller = new XmlUnmarshaller();
		xmlUnmarshaller.UnmarshalXml();
		getDataByPno12("ABCDEEX");
	}
	
	static void getDataByPno12(String pno12) {
		try {
            DatabaseManager dbMgr = new DatabaseManager();
            dbMgr.getDataByPno12(pno12);
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
	}
	
    static void insertIntoDB() {
    	try {
            DatabaseManager dbMgr = new DatabaseManager();
            long retVal = dbMgr.insertData(interiorResponse);
            if(retVal == uniqeIndexErrorCode || retVal == -1 ) {
            	System.out.println("This row already exit in the table. Handle error");
            	return;
            }
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
    }

}
