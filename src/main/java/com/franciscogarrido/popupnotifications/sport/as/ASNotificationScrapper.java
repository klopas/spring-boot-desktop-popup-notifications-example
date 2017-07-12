package com.franciscogarrido.popupnotifications.sport.as;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ASNotificationScrapper extends Observable implements Runnable {

	private List<String> messagesReaded = new ArrayList<String>();

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		try {
			Document document = Jsoup.connect("http://as.com/#")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
					.referrer("http://www.google.com").timeout(12000).get();
			Elements elements = document.select("h2.title");
			for (Element e : elements) {
				messagesReaded.add(e.text());
			}

			while (true) {
				document = Jsoup.connect("http://as.com/#").get();
				elements = document.select("h2.title");
				for (Element e : elements) {
					final String m = e.text();
					if (!messagesReaded.contains(m)) {
						System.out.println(Thread.currentThread().getName() + ": " + m);
						messagesReaded.add(m);

						setChanged();
						notifyObservers("SPORT:AS:"+m);
					}
				}

				Thread.currentThread().sleep(60000);
			}
		} catch (HttpStatusException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main test.
	 * @param args
	 */
	public static void main(String[] args) {
		new Thread(new ASNotificationScrapper()).start();
	}

}
