package com.ih.utils;

import com.ih.ui.ConnectionDialog;

import com.ih.connection.ServerInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Shehata Ibrahim
 * @since <22-5-2011>
 * @version 1.0 General Purpose SAX Parser used to read XML file targeting some
 *          specific node to save its value to a java bean
 */
public abstract class XMLSAXParser extends DefaultHandler {

	private String currentPath = ""; // Used to save current Node Path
	private HashMap<String, String> valuesMap = new HashMap<String, String>(); // Used
																				// Save
																				// (Path,Value)
																				// pairs
	private int insertionCount = 0;
	private int invalidCount = 0;
	private String targetNode; // The Tag specifies the Object start
	boolean objectStarted = false; // Flag to record that object start
	boolean writingXML = false; // Flag to record if there is XML Structure
								// saving is currently running
	String currentXML = ""; // Current stored XML Structure
	String currentXMLPath = ""; // Current XML Path
	HashSet<String> xmlNodes = new HashSet<String>(); // Set of nodes needs to
														// have its XML
														// structure
														// saved
	int bulkSize = 1000;
	ArrayList<HashMap<String, String>> nodesList = new ArrayList<HashMap<String, String>>();
	private String errors = new String();// used for logging parsing errors
	

	public String getErrors() {
		if (errors == null || errors.isEmpty())
			return null;
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	private HashMap<String, Object> paramList = new HashMap<String, Object>();

	public void setParameter(String paramName, Object value) {
		if (!paramList.containsKey(paramName))
			paramList.put(paramName, value);
		
	}

	public Object getParameter(String paramName) {
		return paramList.get(paramName);
	}

	/**
	 * 
	 * @param ObjectNode
	 *            : Defines the tag where the Object starts
	 */
	public XMLSAXParser(String ObjectNode) {
		targetNode = ObjectNode;
	}

	/**
	 * 
	 * @return Set of all XML Paths needs to be Saved as XML Structure
	 */
	public HashSet<String> getXmlNodes() {
		return xmlNodes;
	}

	/**
	 * 
	 * @param xmlNodes
	 *            Set of all XML Paths needs to be Saved as XML Structure
	 */
	public void setXmlNodes(HashSet<String> xmlNodes) {
		this.xmlNodes = xmlNodes;
	}

	/**
	 * 
	 * @param xmlMap
	 *            Node Object XML saved in Path,Value Pairs that will be used in
	 *            insertion process
	 */
	public abstract void insertObjects(
			ArrayList<HashMap<String, String>> nodeList);

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (!qName.equalsIgnoreCase(targetNode)) { // if it is not the target
													// node
													// that starts the object
													// recording and the object
													// didn't start yet ... node
													// will be discarded
			if (!objectStarted)
				return;
		} else
		// if it is the target node then turn the flag to record that the
		// object started
		{
			objectStarted = true;
		}
		if (xmlNodes.contains(currentPath + "/" + qName)) { // Check if current
															// tag is an XML Tag
			writingXML = true; // change the flag to indicate that XML structure
								// capping started
			currentXMLPath = currentPath + "/" + qName; // save the current Path
														// of the XML to be
														// saved as XML Key
		}
		if (writingXML) {// Start recording the XML structure "Tags /Attributes"
			currentXML += "<" + qName;
			for (int i = 0; i < attributes.getLength(); i++) {
				currentXML += " " + attributes.getQName(i) + "=\""
						+ attributes.getValue(i) + "\"";
			}
			currentXML += ">";
		}
		currentPath += "/" + qName; // add Current tag the whole Path
		for (int i = 0; i < attributes.getLength(); i++) {
			// add all attributes as /path/attribute-name$ , value
			valuesMap.put(currentPath + "/" + attributes.getQName(i) + "$",
					attributes.getValue(i));
		}

	}

	@Override
	public void characters(char[] buf, int offset, int len) throws SAXException {
		if (!objectStarted)
			return;
		// get current value
		String s = new String(buf, offset, len);
		if (!s.trim().isEmpty()) {
			if(valuesMap.containsKey(currentPath))
			{
				valuesMap.put(currentPath, valuesMap.get(currentPath)+","+s);
			}else
			{
			valuesMap.put(currentPath, s); // save path,value Pair
			}
			if (writingXML) // if XML structure capping running ..add the
							// current string to the current XML structure
				currentXML += s;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (writingXML) {// if capping XML save keep constructing the XML
							// Structure
			currentXML += "</" + qName + ">";
		}
		if (xmlNodes.contains(currentPath)) { // if reached the end of the XML
												// structure capping save the
												// path,value Pair and reset
												// everything
			writingXML = false;
			valuesMap.put(currentXMLPath, currentXML);
			currentXML = "";
			currentXMLPath = "";
		}
		if (currentPath.contains("/" + qName)) { // remove the current tag from
													// the recorded path
			currentPath = currentPath.substring(0,
					currentPath.lastIndexOf("/" + qName));
		}
		if (qName.equalsIgnoreCase(targetNode)) { // if reached the end of the
													// object
													// 1-call the abstract
													// method
													// 2-reset the path,value
													// map
													// 3- change the flag to
													// search for a new object
			nodesList.add(valuesMap);
			if (nodesList.size() >= bulkSize) {
				insertObjects(nodesList);
				nodesList = new ArrayList<HashMap<String, String>>();
			}
			valuesMap = new HashMap<String, String>();
			objectStarted = false;

		}

	}

	public int getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(int bulkSize) {
		this.bulkSize = bulkSize;
	}

	/*
	 * Date startdate=null; // Performance Testing Part
	 * 
	 * @Override public void startDocument() throws SAXException { startdate=new
	 * Date();
	 * 
	 * }
	 */
	@Override
	public void endDocument() throws SAXException {
		if (nodesList.size() > 0)
			insertObjects(nodesList);
		/*
		 * Date enddate=new Date(); SimpleDateFormat f=new
		 * SimpleDateFormat("HH:mm:ss:SS");
		 * System.out.println(f.format(startdate));
		 * System.out.println(f.format(enddate));
		 */

	}

	public void setInsertionCount(int insertionCount) {
		this.insertionCount = insertionCount;
	}

	public int getInsertionCount() {
		return insertionCount;
	}

	public void setInvalidCount(int invalidCount) {
		this.invalidCount = invalidCount;
	}

	public int getInvalidCount() {
		return invalidCount;
	}
}
