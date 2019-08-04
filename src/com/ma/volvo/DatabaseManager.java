package com.ma.volvo;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "system";
	private String pass = "dev";
	private static Connection conn;
	long uniqeIndexErrorCode = 23000l;

	private static final String INSERT_INTERIOR_ROOMS_MASTER = "INSERT INTO INTERIOR_ROOMS_MASTER"
								+ "(STR_WEEK_FROM, STR_WEEK_TO, PNO12, COLOR, UPHOLSTERY) " 
								+ "VALUES(?, ?, ?, ?, ?)";
	private static final String INSERT_INTERIOR_ROOMS_FEATURES = "INSERT INTO INTERIOR_ROOMS_FEATURES "
								+ "(MASTER_ROOM_ID, DATA_ELEMENT,STATE,CODE, COMMON) " 
								+ "VALUES( ?, ?, ?, ?, ? )";

	private static final String SELECT_MASTER_AND_FEATURE = "SELECT * FROM INTERIOR_ROOMS_MASTER master "
								+ "JOIN INTERIOR_ROOMS_FEATURES feature on " 
								+ "feature.master_room_id = master.room_id "
								+ "WHERE master.pno12 = ?";
	
	private final String ROOM_ID = "ROOM_ID";
	private final String STR_WEEK_FROM = "STR_WEEK_FROM";
	private final String STR_WEEK_TO = "STR_WEEK_TO";
	private final String PNO12 = "PNO12";
	private final String COLOR = "COLOR";
	private final String UPHOLSTERY = "UPHOLSTERY";
	private final String MASTER_ROOM_ID = "MASTER_ROOM_ID";
	private final String DATA_ELEMENT = "DATA_ELEMENT";
	private final String STATE = "STATE";
	private final String CODE = "CODE";
	
	private String[] columnNames = {ROOM_ID, STR_WEEK_FROM, STR_WEEK_TO, PNO12, COLOR, UPHOLSTERY,  
									MASTER_ROOM_ID, DATA_ELEMENT,STATE, CODE};  

	public DatabaseManager() {
		try {
			conn = DriverManager.getConnection(url, user, pass);
			if (conn != null) {
				System.out.println("Connected to the database!");
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long insertData(InteriorResponse interiorResponse) {
		long masterRetVal = -1l;
		try {
			for (ColorUpholstery colUph : interiorResponse.getColorUpholsteryList()) {
				masterRetVal = insertIntoInteriorMaster(interiorResponse.getStartWeek(), interiorResponse.getEndWeek(),
						interiorResponse.getPno12(), colUph.getColor(), colUph.getUpholstery());

				System.out.println("Master insert primary key: " + masterRetVal);
				if (masterRetVal == uniqeIndexErrorCode) {
					System.out.println("This row already exit in the table. Handle error");
				}
			}
		} catch (Exception e) {
			System.out.println("Error when doing  insert . Handle error " + e.getMessage());
		}
		long retVal = insertCommonFeaturData(interiorResponse, masterRetVal);
		if (retVal == -1) {
			System.out.println("Error to insert feature data");
		}
		retVal = insertIndividualFeature(interiorResponse, masterRetVal);
		if (retVal == -1) {
			System.out.println("Error to insert individual data");
		}
		return masterRetVal;
	}

	private Long insertIntoInteriorMaster(int startWeek, int endWeek, String pno12, String color, String upholstery) {
		String key[] = { "ROOM_ID" };
		Long retValue = -1l;
		PreparedStatement pst = null;
		ResultSet rset = null;
		try {
			pst = conn.prepareStatement(INSERT_INTERIOR_ROOMS_MASTER, key);
			pst.setInt(1, startWeek);
			pst.setInt(2, endWeek);
			pst.setString(3, pno12);
			pst.setString(4, color);
			pst.setString(5, upholstery);
			pst.executeUpdate();
			rset = pst.getGeneratedKeys();
			if (rset.next()) {
				retValue = rset.getLong(1);
				System.out.println("Master Primary key: " + retValue);
			}
		} catch (SQLException e) {
			// if e.getSQLState() returns 23000. It means integrity constraint violation of
			// the unique index
			retValue = convertErroCode(e.getSQLState());
		} catch (Exception e) {
			System.out.println("Exception when insert into INTERIOR_ROOMS_MASTER");
		}
		return retValue;
	}

	private long insertCommonFeaturData(InteriorResponse interiorResponse, long masterId) {
		long retVal = -1l;
		PreparedStatement pst = null;
		ResultSet rset = null;
		for (Feature feature : interiorResponse.getFeatureList()) {
			retVal = insertFeatureData(masterId, feature.getCode(), null, "code unknown", "1");
			System.out.println("Common feature insert: " + retVal);
			if (retVal == -1l) {
				System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
			}
		}

		for (Integer option : interiorResponse.getOptionList()) {
			retVal = insertFeatureData(masterId, option, null, "code unknown", "1");
			System.out.println("Common option insert: " + retVal);
			if (retVal == -1l) {
				System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
			}
		}
		if (retVal == -1) {
			System.out.println("Error to insert common data");
		}
		return retVal;
	}

	public long insertIndividualFeature(InteriorResponse interiorResponse, long masterId) {
		long retVal = -1;
		for (InteriorRoom interiorRoom : interiorResponse.getCuList()) {
			for (Feature feature : interiorRoom.getFeatureList()) {
				retVal = insertFeatureData(masterId, feature.getCode(), null, "code unknown", "0");
				System.out.println("Individual feature insert : " + retVal);
				if (retVal == -1l) {
					System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
				}
			}
			for (Option option : interiorRoom.getOptionList()) {
				retVal = insertFeatureData(masterId, option.getCode(), option.getState(), "code unknown", "0");
				System.out.println("individual option insert: " + retVal);
				if (retVal == -1l) {
					System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
				}
			}
		}
		return retVal;
	}

	private long insertFeatureData(long masterId, long dataElement, String state, String code, String common) {
		long retVal = -1l;
		PreparedStatement pst = null;
		ResultSet rset = null;
		try {
			pst = conn.prepareStatement(INSERT_INTERIOR_ROOMS_FEATURES);
			pst.setLong(1, masterId);
			pst.setLong(2, dataElement);
			pst.setString(3, null);
			pst.setString(4, "code unknown");
			pst.setString(5, "1");
			rset = pst.executeQuery();
			if (rset.next() == true) {
				retVal = 1l;
			} else {
				retVal = -1l;
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
		}
		return retVal;
	}


	public ArrayList<String> getDataByPno12(String pno12) {
		PreparedStatement pst = null;
		ResultSet rset = null;
		ArrayList<String> data = new ArrayList<String>();
		Map<Integer, Map<String, String>> dataMap = new HashMap<Integer, Map<String, String>>();
		int index = 0;
		try {
			pst = conn.prepareStatement(SELECT_MASTER_AND_FEATURE);
			pst.setString(1, pno12);
			rset = pst.executeQuery();
			while (rset.next()) {
				Map<String, String> map = new HashMap<String, String>();
				for (String col : columnNames) {
			        String value = rset.getString(col);
			        map.put(col, value);
			        data.add(value);
			    }
			    dataMap.put(index, map);
			    index++;
			}
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
		}
		for (String val : data) {
			System.out.println("data : " + val);
		}

		for (Integer key : dataMap.keySet()) {
			Map<String, String> map = dataMap.get(key);
			for (String key2 : map.keySet()) {
				System.out.println("key: " + key2 + " val: " + dataMap.get(key2));
			}
		}
		return data;
	}

	private Long convertErroCode(String error) {
		long errorCode = -1;
		try {
			errorCode = (long) Long.parseLong(error);
		} catch (Exception ex) {
			System.out.println("Excepiton to convert sql error code string to long");
		}
		return errorCode;
	}
}
