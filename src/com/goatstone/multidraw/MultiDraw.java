package com.goatstone.multidraw;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

public class MultiDraw {

	private static Sender sender;
	private static final String className = MultiDraw.class.getName();
 	private static MulticastResult multicastResult;
	private static DatastoreService datastore = DatastoreServiceFactory
			.getDatastoreService();
	private static String apiKey = "";
	
	public MultiDraw() {
		
		datastore = DatastoreServiceFactory.getDatastoreService();
 		
		
//		Entity settings = new Entity("Settings" );
//		settings.setProperty("ApiKey", "AIzaSyCMGnX-UA_GJ-dICWRwiETe56zxGFpGZRI");
//		datastore.put(settings);
//
//		Entity device = new Entity("device" );
////		device.setProperty("deviceId", "APA91bE57FyheJ7z3be2IchYJnKyteLbZlISRzAXgnE_1djl755n87IVXzpimWcaBl0QkjSHlNb_uaJ2Hm1ea_QDWz86nhpFbQL7UBzkMvPvCp2FEfYfty6VTuqwLFcoUGRcBigfw1m534XddWDRMSNsFvuAQpe6mT10Ze4cck7smkOAu7Eg6_I");
//		device.setProperty("deviceId", "APA91bFg0rchPrZzeasrU9jI0JuxJlzA_RWMNZVXMVaH7gVc2-V6MUjWWf5Ud-bVmnv_BGEueiJ1r2U1b8VlYVQoMmo3eZlVv1hQGVqNINu0JpSpfwXvrmuhIGnAwq2-MK7bgEZSj8lV7XOBv6AWyyTqlFl30ffgFw5iVZOYvkUYVztXCTTZdiM");
//		datastore.put(device);
		
		Query apiKeyQ = new Query("Settings");
		PreparedQuery pq = datastore.prepare(apiKeyQ);
		List<Entity> dIds2 = datastore.prepare(apiKeyQ).asList(
				FetchOptions.Builder.withLimit(1));
		apiKey = 	dIds2.get(0).getProperty("ApiKey").toString();	
 		sender = new Sender(apiKey);
	}

	public static void broadcastMessag(Message message) {

 		try {
			multicastResult = sender.sendNoRetry(message, getDeviceList());

		} catch (IOException e) {
			e.printStackTrace();
			Util.log("broadcastMessag : fail", className);
			return;
		}
		boolean allDone = true;
		// onNewGCMRegistrationIdProvided() ::
		// onMultiCastFailure() :: retry or unregister
		if (allDone) {
		} else {
			// retryTask ;
		}
	}
	private static ArrayList<String> getDeviceList() {
		Query devideIdsQ = new Query("device");
		List<Entity> dIds = datastore.prepare(devideIdsQ).asList(
				FetchOptions.Builder.withLimit(1000));

		ArrayList<String> idList = new ArrayList<String>();
		for (Entity e : dIds) {
			String deviceId = (String) e.getProperty("deviceId");
			idList.add(deviceId);
		}
		return idList;
	}

	public static void addDevice(String deviceId) {
		Entity device = new Entity("device", deviceId);
		device.setProperty("deviceId", deviceId);
		device.setProperty("name", "user name");
		device.setProperty("num", 1);
		device.setProperty("reg_date", new Date());
		datastore.put(device);
	}

	public static void removeDevice(String deviceId) {
		Filter filter1 = new FilterPredicate("deviceId", FilterOperator.EQUAL,
				deviceId);
		Query devideIdsQ = new Query("device").setFilter(filter1);
		Entity e = datastore.prepare(devideIdsQ).asSingleEntity();
		if (e != null) {
			Transaction txn = datastore.beginTransaction();
			datastore.delete(txn, e.getKey());
			txn.commit();
		}
	}
 
	public static void register(String deviceId) {
 		addDevice(deviceId);
 	}

	public static void unregister(String deviceId) {
 		removeDevice(deviceId);
	}

	private void onNewGCMRegistrationIdProvided() {
		if (multicastResult.getCanonicalIds() != 0) {
			List<Result> results = multicastResult.getResults();
			for (int i = 0; i < results.size(); i++) {
				String canonicalRegId = results.get(i)
						.getCanonicalRegistrationId();
				if (canonicalRegId != null) {
					// String regId = regIds.get(i);
					// HelloGCM.updateRegistration(regId, canonicalRegId);
				}
			}
		}
	}

	private void onMultiCastFailure() {
		if (multicastResult.getFailure() != 0) {
			// there were failures, check if any could be retried
			List<Result> results = multicastResult.getResults();
			List<String> retriableRegIds = new ArrayList<String>();
			for (int i = 0; i < results.size(); i++) {
				String error = results.get(i).getErrorCodeName();
				if (error != null) {
					// String regId = regIds.get(i);
					// logger.warning("Got error (" + error + ") for regId " +
					// regId);
					if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
						// application, removed from device - unregister it
						// HelloGCM.unregister(regId);
					}
					if (error.equals(Constants.ERROR_UNAVAILABLE)) {
						// retriableRegIds.add(regId);
					}
				}
			}
			if (!retriableRegIds.isEmpty()) {
				// DS.updateMulticast(multicastKey, retriableRegIds);
				// retryTask;
			}
		}
	}
 
}
