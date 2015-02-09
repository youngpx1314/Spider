package com.youngpx1314.spider.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicePool {
	private static final ExecutorService service = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	public static ExecutorService getExecutorService() {
		return service;
	}
	public static void tryClosePool() {
		if (!service.isShutdown()) {
			service.shutdown();
		}
	}
}
