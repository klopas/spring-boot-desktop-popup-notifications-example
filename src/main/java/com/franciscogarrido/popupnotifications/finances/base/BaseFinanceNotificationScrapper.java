package com.franciscogarrido.popupnotifications.finances.base;

import java.io.IOException;
import java.util.Observable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public abstract class BaseFinanceNotificationScrapper extends Observable implements Runnable {

	private String previousValue;

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			Document document;
			Elements elements;

			while (true) {
				document = Jsoup.connect(getUrl())
						.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
						.referrer("http://www.google.com").timeout(12000).get();
				elements = document.select("span#last_last");
				final String m = elements.get(0).text();
//				System.out.println(Thread.currentThread().getName() + ": " + m);
				if (!m.equals(previousValue)) {
					previousValue = m;

					setChanged();
					notifyObservers("FINANCES:"+getName()+":"+m);
				}

				Thread.currentThread().sleep(60000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	protected abstract String getUrl();
	
	protected abstract String getName();

}
