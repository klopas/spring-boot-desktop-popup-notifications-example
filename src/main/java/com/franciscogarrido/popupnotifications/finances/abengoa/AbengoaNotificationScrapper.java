package com.franciscogarrido.popupnotifications.finances.abengoa;

import com.franciscogarrido.popupnotifications.finances.base.BaseFinanceNotificationScrapper;

public class AbengoaNotificationScrapper extends BaseFinanceNotificationScrapper implements Runnable {

	@Override
	protected String getUrl() {
		return "https://www.investing.com/equities/abengoa";
	}

	@Override
	protected String getName() {
		return "ABENGOA";
	}
	
	/**
	 * Main for testing.
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new AbengoaNotificationScrapper()).start();
	}

}
