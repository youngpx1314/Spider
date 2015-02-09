package com.youngpx1314.spider.loader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.youngpx1314.spider.analyser.Analyser;
import com.youngpx1314.spider.pool.ServicePool;

/**
 * @author youngpx1314
 *
 */
public class HttpLoader {
	private String httpPath;

	public HttpLoader(String httpPath) {
		this.httpPath = httpPath;
	}

	public String load() {
		BufferedInputStream bis = null;
		HttpURLConnection connection = null;
		StringBuilder str = new StringBuilder();
		try {
			URL url = new URL(this.httpPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			bis = new BufferedInputStream(url.openStream());
			byte[] bytearray = new byte[1024];
			int num = 0;
			while ((num = bis.read(bytearray)) != -1) {
				str.append(new String(bytearray));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null)
				connection.disconnect();
		}
		return str.toString();
	}
	public void startAnalyse(final String str) {
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		tasks.add(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println("task called");
				Analyser analyser = new Analyser(str);
				analyser.startLoader(analyser.analyse());
				return null;
			}
		});
		try {
			ServicePool.getExecutorService().invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
