package com.ma.volvo;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface InteriorService {
	
	@WebMethod
	ArrayList<String> getDataByPno12(String pno12);
	
	@WebMethod
	void UnmarshallXml();

}
