/*
 *  Copyright 2016 Carnegie Mellon University
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
package edu.cmu.lti.oaqa.apps;

import java.io.*;

import org.w3c.dom.Document;

import edu.cmu.lti.oaqa.leoutils.*;

import org.w3c.dom.*;

public class SampleUse {
  public static final int MAX_LEN = 16;

  public static void main(String[] args) {
    try {
      InputStream istr = CompressUtils.createInputStream("sample_data/wiki_sample.txt.bz2");
      
      XmlIterator xiter = new XmlIterator(istr, "page");
      
      String chunk;
      while (!(chunk = xiter.readNext()).isEmpty()) {
        Document  rootNode = XmlHelper.parseDocWithoutXMLDecl(chunk);
        Node      pageNode = XmlHelper.getNode("page", rootNode.getChildNodes());
        Node      titleNode = XmlHelper.getNode("title", pageNode.getChildNodes());        
        String    title  = titleNode != null ? XmlHelper.getNodeValue(titleNode) : "";
        Node      revisionNode = XmlHelper.getNode("revision", pageNode.getChildNodes());
        Node   textNode = null;
        if (revisionNode != null) textNode = XmlHelper.getNode("text", revisionNode.getChildNodes()); 
        String text = "";
        if (textNode != null)
          text = XmlHelper.getNodeValue(textNode);
        if (text.length() > MAX_LEN) text = text.substring(0, MAX_LEN) + " ... ";
        
        System.out.println(title);
        System.out.println("----------------------------");
        System.out.println(text);
        System.out.println("============================");
      }
      
    } catch (Exception e) {
      System.err.println("Some failure: " + e);
      e.printStackTrace();
    }
  }

}
