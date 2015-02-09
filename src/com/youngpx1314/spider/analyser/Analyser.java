package com.youngpx1314.spider.analyser;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author youngpx1314
 *
 */
public class Analyser {
	private String body;
	public Analyser(String body) {
		this.body = body;
	}
	public List<String> analyse() {
		final List<String> list = new ArrayList<String>();
		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();
			DefaultHandler handler = new DefaultHandler() {
				@Override
				public void startElement(String url, String localName,
						String qName,
						Attributes attributes) throws SAXException {
					if ("a".equalsIgnoreCase(qName)) {
						list.add(attributes.getValue("href"));
					}

				}
			};
			parser.setContentHandler(handler);
			parser.setDTDHandler(handler);
			parser.setEntityResolver(handler);
			parser.setErrorHandler(handler);
			parser.parse(new InputSource(new ByteArrayInputStream(this.body
					.getBytes(Charset.forName("UTF-8")))));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public void startLoader(final List<String> https) {
	}
}
