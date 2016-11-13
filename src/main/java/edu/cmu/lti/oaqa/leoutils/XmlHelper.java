/*
 *  Copyright 2014 Carnegie Mellon University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package edu.cmu.lti.oaqa.leoutils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * A bunch of useful functions to work with XML files. In particular,
 * with XML files that store data for indexable fields. 
 * 
 * <p>Two notable XML formats:
 * <ul>
 * <li>A generic simple two-level XML (DOC -&gt; FIELD_NAME -&gt; FIELD_CONTENT);
 * <li>A more complex, more deeply nested, AQUAINT format, which we call
 * a three-level format. See {@link #parseXMLAQUAINTEntry(String)} for details.
 * </ul>
 * 
 * @author Leonid Boytsov
 *
 */
public class XmlHelper {
  public static final String XML_VERSION = "1.0";
  public static final String ENCODING_NAME = "UTF-8";
  /**
  
  /*
   * Functions getNode and getNodeValue are 
   * based on the code from Dr. Dobbs Journal:
   * http://www.drdobbs.com/jvm/easy-dom-parsing-in-java/231002580
   */
  
  /**
   * Find node by node while ignoring the case.
   * 
   * @param tagName     node tag (case-insensitive).
   * @param nodes       a list of nodes to look through.
   * @return a node name if the node is found, or null otherwise.
   */
  public static Node getNode(String tagName, NodeList nodes) {
    for ( int x = 0; x < nodes.getLength(); x++ ) {
        Node node = nodes.item(x);
        if (node.getNodeName().equalsIgnoreCase(tagName)) {
            return node;
        }
    }
 
    return null;
  }
 
  /**
   * Get textual content of a node(for text nodes only).
   * 
   * @param node a text node.
   * @return node's textual content.
   */
  public static String getNodeValue(Node node ) {
    NodeList childNodes = node.getChildNodes();
    for (int x = 0; x < childNodes.getLength(); x++ ) {
        Node data = childNodes.item(x);
        if ( data.getNodeType() == Node.TEXT_NODE )
            return data.getTextContent();
    }
    return "";
  }
  
  /**
   * 
   * Removes an XML declaration.
   * 
   * @param docStr input string
   * @return an XML without declaration.
   */
  public String removeHeader(String docStr) {
    // Let's remove <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    int indx = docStr.indexOf('>');
    // If indx == -1, indx + 1 == 0 => we output the complete string
    String docNoXmlHead = docStr.substring(indx + 1);
    return docNoXmlHead;    
  }
      
  /**
   * Parses an XML document that comes without an XML declaration.
   * 
   * @param docLine a textual representation of XML document.
   * @return an object of the type {@link org.w3c.dom.Document}.
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public static Document parseDocWithoutXMLDecl(String docLine) 
      throws ParserConfigurationException, SAXException, IOException {
    String xml = String.format(
        "<?xml version=\"%s\"  encoding=\"%s\" ?>%s",
        XML_VERSION, ENCODING_NAME, docLine);
    return parseDocument(xml);

  }
  
  /**
   * A wrapper function to parse XML represented by a string.
   * 
   * @param docText     a textual representation of an XML document.
   * @return an object of the type {@link org.w3c.dom.Document}.
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   */
  public static Document parseDocument(String docText) 
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder dbld = 
        DocumentBuilderFactory.newInstance().newDocumentBuilder();
    return dbld.parse(new InputSource(new StringReader(docText)));
  }  

  private final static String NL = System.getProperty("line.separator");
}
