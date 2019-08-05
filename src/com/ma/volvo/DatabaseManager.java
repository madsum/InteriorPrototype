package com.ma.volvo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseManager {

	/*
    private String url = "jdbc:oracle:thin:@GOTSVL2290.got.volvocars.net:1521:dpgccd";
    private String user = "gcc_dbs_dev_admin";
    private String pass = "gcc_dbs_dev_admin";
    */
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

    private static final String SELECT_MASTER_AND_FEATURE_BY_PNO12 = "SELECT DATA_ELEMENT, STATE, CODE, COMMON FROM INTERIOR_ROOMS_MASTER master "
								+ "JOIN INTERIOR_ROOMS_FEATURES feature on " 
								+ "feature.master_room_id = master.room_id "
								+ "WHERE master.pno12 = ?";

    private static final String SELECT_MASTER_AND_FEATURE_BY_ALL = "SELECT DATA_ELEMENT, STATE, CODE, COMMON FROM INTERIOR_ROOMS_MASTER master "
        + "JOIN INTERIOR_ROOMS_FEATURES feature on " + "feature.master_room_id = master.room_id " + "WHERE master.pno12 = ? AND "
        + "master.str_week_from = ? AND " + "master.str_week_to = ? AND " + "master.color = ? AND " + "master.upholstery = ?";

    private static final int ELEMENT_STANDARDFEATURE = 115;
    private static final int ELEMENT_OPTION = 12;
	
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
    private final String COMMON = "COMMON";
	
    private String[] allColumnNames = { ROOM_ID, STR_WEEK_FROM, STR_WEEK_TO, PNO12, COLOR, UPHOLSTERY,
        MASTER_ROOM_ID, DATA_ELEMENT, STATE, CODE, COMMON };

    private ArrayList<Feature> individualFearturs = new ArrayList<Feature>();
    private ArrayList<Option> individualOptions = new ArrayList<Option>();
    private ArrayList<Feature> commonFearturs = new ArrayList<Feature>();
    private ArrayList<Option> commonOptions = new ArrayList<Option>();
	private ArrayList<String> data = new ArrayList<String>();


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
			masterRetVal = insertIntoInteriorMaster(interiorResponse.getStartWeek(), interiorResponse.getEndWeek(),
					interiorResponse.getPno12(), interiorResponse.getColor(), interiorResponse.getUpholstrey());

			System.out.println("Master insert primary key: " + masterRetVal);
			if (masterRetVal == uniqeIndexErrorCode) {
				System.out.println("This row already exit in the table. Handle error");
			}
		} catch (Exception e) {
			System.out.println("Error when doing  insert . Handle error " + e.getMessage());
		}
		long retVal = insertCommonFeaturData(interiorResponse, masterRetVal);
		if (retVal == -1) {
			System.out.println("Error to insert feature data");
		}
		retVal = insertIndividualFeatureData(interiorResponse, masterRetVal);
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
            retVal = insertFeatureData(masterId, ELEMENT_STANDARDFEATURE, null, feature.getCode(), "1");
			System.out.println("Common feature insert: " + retVal);
			if (retVal == -1l) {
				System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
			}
		}

        for (String option : interiorResponse.getOptionList()) {
            retVal = insertFeatureData(masterId, ELEMENT_OPTION, null, option, "1");
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

	public long insertIndividualFeatureData(InteriorResponse interiorResponse, long masterId) {
		long retVal = -1;
		for (InteriorRoom interiorRoom : interiorResponse.getCuList()) {
			for (Feature feature : interiorRoom.getFeatureList()) {
                retVal = insertFeatureData(masterId, ELEMENT_STANDARDFEATURE, null, feature.getCode(), "0");
				System.out.println("Individual feature insert : " + retVal);
				if (retVal == -1l) {
					System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
				}
			}
			for (Option option : interiorRoom.getOptionList()) {
                retVal = insertFeatureData(masterId, ELEMENT_OPTION, option.getState(), option.getCode(),  "0");
				System.out.println("individual option insert: " + retVal);
				if (retVal == -1l) {
					System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
				}
			}
		}
		return retVal;
	}

    private long insertFeatureData(long masterId, int dataElement, String state, String code, String common) {
		long retVal = -1l;
		PreparedStatement pst = null;
		ResultSet rset = null;
		try {
			pst = conn.prepareStatement(INSERT_INTERIOR_ROOMS_FEATURES);
			pst.setLong(1, masterId);
            pst.setInt(2, dataElement);
            pst.setString(3, state);
            pst.setString(4, code);
            pst.setString(5, common);
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
		try {
            pst = conn.prepareStatement(SELECT_MASTER_AND_FEATURE_BY_PNO12);
			pst.setString(1, pno12);
			rset = pst.executeQuery();
            addDataInList(rset);
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
		}
        return data;
	}
	
    public ArrayList<String> getDataByAll(String pno12, long str_week_from, long str_week_to, String color, String upholstery) {
        PreparedStatement pst = null;
        ResultSet rset = null;
        try {
            pst = conn.prepareStatement(SELECT_MASTER_AND_FEATURE_BY_ALL);
            pst.setString(1, pno12);
            pst.setLong(2, str_week_from);
            pst.setLong(3, str_week_to);
            pst.setString(4, color);
            pst.setString(5, upholstery);

            rset = pst.executeQuery();
            addDataInList(rset);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
        }
        printData();
        return data;
    }

    private void addDataInList(ResultSet rset) {
        try {
            while (rset.next()) {
                if (rset.getLong(DATA_ELEMENT) == 115 && rset.getString(COMMON).equalsIgnoreCase("1")) {
                    Feature feature = new Feature();
                    feature.setCode(rset.getString(CODE));
                    commonFearturs.add(feature);
                    data.add(rset.getString(CODE));
                } else if (rset.getLong(DATA_ELEMENT) == 12 && rset.getString(COMMON).equalsIgnoreCase("1")) {
                    Option option = new Option();
                    option.setCode(rset.getString(CODE));
                    option.setState(rset.getString(STATE));
                    commonOptions.add(option);
                    data.add(rset.getString(STATE));
                } else if (rset.getLong(DATA_ELEMENT) == 115 && rset.getString(COMMON).equalsIgnoreCase("0")) {
                    Feature feature = new Feature();
                    feature.setCode(rset.getString(CODE));
                    individualFearturs.add(feature);
                    data.add(rset.getString(CODE));
                } else if (rset.getLong(DATA_ELEMENT) == 12 && rset.getString(COMMON).equalsIgnoreCase("0")) {
                    Option option = new Option();
                    option.setCode(rset.getString(CODE));
                    option.setState(rset.getString(STATE));
                    individualOptions.add(option);
                    data.add(rset.getString(CODE));
                    data.add(rset.getString(STATE));
                }
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
        }
        printData();
    }

    private void printData() {
        for (Feature f : commonFearturs) {
            System.out.println("common featuer code: " + f.getCode());
        }
        for (Option o : commonOptions) {
            System.out.println("common optoin code: " + o.getCode());
            System.out.println("common state : " + o.getState());
        }
        for (Feature f : individualFearturs) {
            System.out.println("individual featuer code: " + f.getCode());
        }
        for (Option o : individualOptions) {
            System.out.println("individual optoin code: " + o.getCode());
            System.out.println("individual optoin state : " + o.getState());
        }
    }
    
	public ArrayList<Feature> getIndividualFearturs() {
		return individualFearturs;
	}

	public ArrayList<Option> getIndividualOptions() {
		return individualOptions;
	}

	public ArrayList<Feature> getCommonFearturs() {
		return commonFearturs;
	}

	public ArrayList<Option> getCommonOptions() {
		return commonOptions;
	}

	public void setCommonOptions(ArrayList<Option> commonOptions) {
		this.commonOptions = commonOptions;
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
