package com.example.cmarshall.newsrssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewsDataGatherer extends AsyncTask<String, Void, ArrayList<NewsEntry>>
{
    @Override
    protected ArrayList<NewsEntry> doInBackground(String... params) {
        ArrayList<NewsEntry> result = new ArrayList<>();
        try {
            URL url = new URL(params[0]);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(new InputSource(url.openStream()));
            NodeList nList = doc.getElementsByTagName("item");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    String title = getAttribute(nNode, "title");
                    String description = stripHtml(getAttribute(nNode, "description"));
                    String URL = getAttribute(nNode, "link");
                    String pubDate = getAttribute(nNode, "pubDate");
                    final NewsEntry newEntry = new NewsEntry(title, description, URL, pubDate);
                    String imageURL = getMediaThumbnailURL(nNode);
                    newEntry.imageURL = imageURL;
                    result.add(newEntry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getMediaThumbnailURL(Node node) {
        Element element = (Element)node;
        NodeList nodes = element.getElementsByTagName("media:thumbnail");
        Element elm = (Element)nodes.item(0);
        if(elm == null) {
            return "";
        } else {
            return (elm.getAttribute("url"));
        }
    }

    private String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }

    private String getAttribute(Node node, String attributeName) {
        Element eElement = (Element) node;
        NodeList attributeList = eElement.getElementsByTagName(attributeName);
        Element attributeElement = (Element)attributeList.item(0);
        return attributeElement.getFirstChild().getTextContent();
    }
}
