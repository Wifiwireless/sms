package com.db.test;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.Context;
import javax.naming.NamingException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
public class DbTester {

	private static EJBContainer ejbContainer;

	private static Context ctx;

	
	@org.testng.annotations.Test
	public void test2() throws NamingException {/*
		Object object2 = ctx
				.lookup("java:global/pl4sms-dbaccess/UserDao!com.ecomm.pl4sms.interfaces.UserDaoInterface");
		UserDaoInterface daoInterface = (UserDaoInterface) object2;
		System.out.println(daoInterface.getUserByRole().size());
		//UserDaoInterface daoInterface = (UserDaoInterface) object2;
////System.out.println(daoInterface.getAll().get(0).getClientref());
		
		
		Object object1 = ctx
				.lookup("java:global/pl4sms-dbaccess/GroupDao!com.ecomm.pl4sms.interfaces.GrooupInterface");
		GrooupInterface groupint = (GrooupInterface) object1;
		
		SystemUser systemUser = daoInterface.getUserByUsername("testaccount2@test.com");
	ArrayList<Group> group = groupint.getGroupBySystemUser(systemUser);
	
	
	
	
		Object object2 = ctx
				.lookup("java:global/pl4sms-dbaccess/Sql_Sent_SmsDao!com.ecomm.pl4sms.interfaces.Sql_Sent_Sms_Interface");
		SystemUser systemUser = userDaoInterface.getUserByID(10);

//		Sql_Sent_Sms_Interface sendsmsDaoInterface = (Sql_Sent_Sms_Interface) object2;
		Date startDate = DateUtility.formatInputDate("2016-03-01 00:00:00");
		Date endDate = DateUtility.formatInputDate("2016-03-10 00:00:00");

		sendsmsDaoInterface.getReplyByDateSqlBox(startDate,endDate, systemUser);
		ArrayList<BlockReplyJSON> arrStop = sendsmsDaoInterface.getStopReplyByDate(startDate, endDate, Pl4smsConstantsInterface.arrStopList);
		
//		//System.out.println(arrStop.get(0));
		//Check for duplicate  ctid
		ArrayList<String> ctidarr = new ArrayList<String>();
		ctidarr.add("2141234");
		//System.out.println(sendsmsDaoInterface.checkForDuplicatecTIdSqlBox(
				ctidarr, systemUser));
//	//System.out.println(userDaoInterface.getUserByClientref("ecomm"));
		ArrayList<String> sent_Sms = sendsmsDaoInterface.checkForDuplicatecTIdSqlBox("689f4a41-4b38-4072-b70b-4776d4e27c76", systemUser);
		////System.out.println(sent_Sms.get(0).getDlr_mask());
				//System.out.println(sent_Sms.size());
		
		
//		DashboardInfo_Json sent_Sms = sendsmsDaoInterface.getTodaysinfo(DateUtility.setTimeToZero(), "fake1");
	*/}

//	@org.testng.annotations.Test
	public void verifyUserTest() throws NamingException {
		Object object1 = ctx
				.lookup("java:global/pl4sms-dbaccess/UserVerificationDao!com.ecomm.pl4sms.interfaces.UserVerificationInterface");
		//UserVerificationInterface userVerificationInterface = (UserVerificationInterface) object1;
	
	////System.out.println(userVerificationInterface.getUserByVerifyCode("56eb19ea-aaf7-4d24-ac22-428b8cb5c8f2"));
	
	}
	// @Test
	public void test() throws NamingException {/*
		Object object1 = ctx
				.lookup("java:global/pl4sms-dbaccess/UserDao!com.ecomm.pl4sms.interfaces.UserDaoInterface");
		Object object2 = ctx
				.lookup("java:global/pl4sms-dbaccess/SentSMSDAO!com.ecomm.pl4sms.interfaces.SentSMSInterface");
		Date curDate = new Date();
		dao = (SentSMSInterface) object2;
		userDaoInterface = (UserDaoInterface) object1;
		String startdate = "2016-01-13 00:00:00";
		String enddate = "2016-01-16 00:00:00";
		Date startDate = DateUtility.formatInputDate(startdate);
		Date endDate = DateUtility.formatInputDate(enddate);
		SystemUser systemUser = userDaoInterface.getUserByID(10);
		DashboardInfo_Json duplicate = dao.getTodaysLoadedSMSCountPerUser(
				DateUtility.setTimeToZero(), systemUser);
		////System.out.println(duplicate.getDate());
		
		 * ArrayList<SentSMS> duplicate = dao.getProcessedSMSByGstid(systemUser,
		 * "f9b48e8e-f5bc-4e3b-a342-4eba26e8f231"); JSON_Response json_Response
		 * = generateResponse(duplicate, "f9b48e8e-f5bc-4e3b-a342-4eba26e8f231",
		 * "");
		 * 
		 * Gson gson = new Gson(); String json = gson.toJson(json_Response);
		 * 
		 * ////System.out.println(json);
		 

		// ////System.out.println(duplicate.size());
		//

		// ArrayList<SentSMS> duplicate=
		// dao.iterateAndgetSentSMSByDate(systemUser, startDate, endDate, 1,40);
		// ArrayList<SentSMS> duplicate= dao.getSentSMSByDate(startDate,
		// endDate, systemUser, 1, 40);

		// fail
		// ArrayList<SentSMS> duplicate =
		// dao.getSentSMSByDateAndStatus(startDate, endDate, systemUser, 1,"8",
		// 40);
		// ArrayList<SentSMS> duplicate =
		// dao.getSentSMSByStartDateAndEnddateAndFinalStatus(startDate, endDate,
		// systemUser);
		// ////System.out.println(sentSMS.size());
		
		 * dao.updateSentSMSByschDateAndFlag(new java.sql.Timestamp(curDate
		 * .getTime()));
		 
		// SystemUser systemUser = userDaoInterface.getUserByID(10);
		// ArrayList<SentSMS> arrsentsms =
		// dao.getSentSMSIDBysTId("ad6f2a77-c855-4733-aa36-43d34e30808c",
		// systemUser);
		// ////System.out.println(duplicate.get(0).toString());
		// ////System.out.println(sentSMS.size());
		// ////System.out.println(sentSMS.get);
		// ////System.out.println(sentSMS.get(0));

		
		 * sentSMS.setDestination("+27813633049"); dao.mergeSentSMS(sentSMS);
		 
		// LOG.debug(dao.getUserByRole());
		// dao.addSentSMS(sentSMS);
		// sendloadedMessage(loadedSMS);
		// SentSMS sentsms = dao.getSentSMsByid(642239);

		// sentsms.setDestination("+27777777777");

	*/}

	/**
	 * test of incoming receipts speed method
	 */
	



	// @Test
	public void insertTest() throws NamingException {/*

		Object object2 = ctx
				.lookup("java:global/pl4sms-dbaccess/SentSMSDAO!com.ecomm.pl4sms.interfaces.SentSMSInterface");
		Date curDate = new Date();
		dao = (SentSMSInterface) object2;

		SentSMS sentSMS = new SentSMS();
		sentSMS.setDestination("27813633038");
		sentSMS.setMessage("hello db test22");
		sentSMS.setSmsc("fake");
		sentSMS.setKannelResponse("loaded");
		sentSMS.setSentDate(curDate);
		sentSMS = dao.addSentSMS(sentSMS);
		sentSMS.setKannelResponse("accept");
		dao.update(sentSMS);
		
		 * ArrayList<SentSMS> arrsentsms =
		 * dao.getSentSMSByDate(startDate,endDate,systemUser,1,5);
		 
		// ////System.out.println(sentSMS.size());
		
		 * dao.updateSentSMSByschDateAndFlag(new java.sql.Timestamp(curDate
		 * .getTime()));
		 
		// SystemUser systemUser = userDaoInterface.getUserByID(10);
		// ArrayList<SentSMS> arrsentsms =
		// dao.getSentSMSIDBysTId("ad6f2a77-c855-4733-aa36-43d34e30808c",
		// systemUser);
		// ////System.out.println(arrsentsms.size());
		// ////System.out.println(sentSMS.size());
		// ////System.out.println(sentSMS.get);
		// ////System.out.println(sentSMS.get(0));

		
		 * sentSMS.setDestination("+27813633049"); dao.mergeSentSMS(sentSMS);
		 
		// LOG.debug(dao.getUserByRole());
		// dao.addSentSMS(sentSMS);
		// sendloadedMessage(loadedSMS);
		// SentSMS sentsms = dao.getSentSMsByid(642239);

		// sentsms.setDestination("+27777777777");

	*/}

	@BeforeClass
	public static void intialize() {
		// creating ejb container
			ejbContainer = EJBContainer.createEJBContainer();
		ctx = ejbContainer.getContext();

	}

	
	
}
