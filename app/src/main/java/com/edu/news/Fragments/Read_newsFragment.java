package com.edu.news.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.edu.news.Activities.WebView;
import com.edu.news.R;
import com.edu.news.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Read_newsFragment extends Fragment {


    ListView listView;


    public Read_newsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_read_news, container, false);
        listView = view.findViewById(R.id.lv_news);
        AsyncTask<String,Void,String> content = new RSSFeed().execute("https://vnexpress.net/rss/thoi-su.rss");
        arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        intent = new Intent(getActivity(), WebView.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = arrayLink.get(position);
                intent.putExtra("openlink",link);
                startActivity(intent);
            }
        });
        return view;
    }
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> arrayLink = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    Intent intent;

    public class RSSFeed extends AsyncTask <String,Void,String>{
        //lay du lieu tu sever
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url =new URL(strings[0]);
                InputStreamReader reader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = "";
                while ((line = bufferedReader.readLine())!= null){
                    content.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content.toString();
        }
        //lay du lieu len cleint
        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            XMLParser xmlParser = new XMLParser();
            try {
                Document document = xmlParser.getDocumented(s);
                NodeList nodeList = document.getElementsByTagName("item");
                String title = "";//lay ve title
                for (int i=0;i<nodeList.getLength();i++){
                    Element element = (Element)nodeList.item(i);//lay ve item i
                    title = xmlParser.getValue(element,"title")+"\n";
                    arrayList.add(title);
                    arrayLink.add(xmlParser.getValue(element,"link"));

                }
                arrayAdapter.notifyDataSetChanged();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }
}
