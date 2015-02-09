/**
 * 
 */
package com.youngpx1314.spider;

import com.youngpx1314.spider.loader.HttpLoader;

/**
 * @author youngpx1314
 *
 */
public class Spider {
	private static final String PATH = "http://www.baidu.com";
	public static void main(String[] args) {
		HttpLoader loader = new HttpLoader(PATH);
		loader.startAnalyse(loader.load());
	}
}
