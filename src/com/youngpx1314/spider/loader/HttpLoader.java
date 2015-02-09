package com.youngpx1314.spider.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
		BufferedOutputStream bos = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(this.httpPath);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			bis = new BufferedInputStream(url.openStream());
			bos = new BufferedOutputStream(this.getFile(url));
			byte[] bytearray = new byte[1024];
			int num = 0;
			while ((num = bis.read(bytearray)) != -1) {
				bos.write(bytearray, 0, num);
			}
			return this.getFilePath(url);
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
			if(bos!=null){
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (connection != null)
				connection.disconnect();
		}
	}
	private FileOutputStream getFile(URL url){
		File file = new File(this.getFilePath(url));
		if(file.exists())
			file.delete();
		try {
			File parent = file.getParentFile();
			if(parent!=null&&!parent.exists()){
				parent.mkdir();
			}
			file.createNewFile();
			return new FileOutputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
			
		
	}
	private String getFilePath(URL url){
		StringBuilder filePath = new StringBuilder();
		filePath.append("F:\\").append(url.getProtocol()).append("\\")
		.append(url.getHost()).append("\\").append(url.getPort())
		.append("\\").append(url.getPath());
		return filePath.toString();
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
