package com.youngpx1314.spider.analyser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.youngpx1314.spider.loader.HttpLoader;
import com.youngpx1314.spider.pool.ServicePool;
/**
 * @author youngpx1314
 *
 */
public class Analyser {
	private String path;
	public Analyser(String path) {
		this.path = path;
	}
	public List<String> analyse() {
		final List<String> list = new ArrayList<String>();
		try {
			Parser parser = new Parser(this.path);
			parser.setEncoding("utf-8");
			TagNameFilter  filter = new TagNameFilter("a");
			
			NodeList nodeList = parser.parse(filter);
			if(nodeList!=null&&nodeList.size()>0){
				for(int i=0;i<nodeList.size();i++){
					LinkTag node = (LinkTag) nodeList.elementAt(i);
					String link = node.getAttribute("href");
					if(link!=null&&link.trim().length()>0){
//						System.out.println(link);
						list.add(link);
					}
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		} 
		return list;
	}
	public void startLoader(final List<String> https) {
		List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		for(int i=0;i<https.size();i++){
			final String http = https.get(i);
			tasks.add(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					HttpLoader loader = new HttpLoader(http);
					loader.startAnalyse(loader.load());
					return null;
				}
			});
		}
		try {
			ServicePool.getExecutorService().invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
