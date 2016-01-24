package com.events.manfromspace.eventsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Events today = new Events(this);
        today.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class Events extends AsyncTask<Void, StringBuilder, Integer> {

        private Context context;
        Activity myActivity;
        private ProgressDialog pDialog;
        List<String> eventsList = new ArrayList<String>();
        List<Event> eventsL = new ArrayList<Event>();
        int index=0;
        int index2=0;

        public Events(Activity activity) {
            super();
            context = activity;
        }

        protected void onPreExecute() {

        }

        protected Integer doInBackground(Void... arg0) {

            try{
                Document doc = Jsoup.connect("https://www.viralagenda.com/pt/braga/guimaraes?page=0&perpage=100").get();
                Elements eventsTitle = doc.select("div.viral-event-title").select("a");
                Elements eventsStartDate = doc.select("li.viral-item.viral-event");
                Elements eventsCity = doc.select("div.viral-event-box").select("a").select("span.varios");
                Elements eventsPlace = doc.select("div.viral-event-box").select("a.viral-event-place,span.varioslocais");
                System.out.println(eventsPlace);
                Time now = new Time();
                now.setToNow();
                System.out.println(now);


                for(Element event : eventsTitle) {
                   eventsList.add(eventsTitle.get(index).text());
                    index++;
                }

                String endHour = "";
                String endDate ="";
                //adicionar novas instancias a Eventos. como a data vem juntamente com as horas, divide-se a string com o delimitador T e parte-se em duas. a primeira é o dia, a segunda a hora
                for(Element event: eventsTitle) {
                    String startDate = eventsStartDate.get(index2).attr("data-date-start").split("T")[0];
                    String startHour = eventsStartDate.get(index2).attr("data-date-start").split("T")[1].substring(0, 5);
                    String city = eventsCity.get(index2).text();
                    String place = eventsPlace.get(index2).text();
                    if(!eventsStartDate.get(index2).attr("data-date-end").equals("")) {
                        endDate = eventsStartDate.get(index2).attr("data-date-end").split("T")[0];
                        endHour = eventsStartDate.get(index2).attr("data-date-end").split("T")[1].substring(0, 5);
                    } else {
                        endHour = "";
                        endDate = "";
                    }
                        eventsL.add(new Event(eventsTitle.get(index2).text(), startDate, startHour, endDate, endHour, place, city, city));

                    index2++;
                }


            }



            catch (Exception e){

                System.out.println(e);
            }
            int id = 0;
          /*  for(String i : eventsList) {
               System.out.println(i);
                id++;
            }
            System.out.println(id);
            */
            for(Event i : eventsL){
                System.out.println(i.getTitle() + " " + i.getStartDate() + " "  + i.getStartHour() + " " + i.getEndDate() + " " + i.getEndHour() + " " + i.getPlace() + " " + i.getCity());

            }
            return 0;
        }
    }
}

class Event{

    String title;
    String startDate;
    String endDate;
    String startHour;
    String endHour;
    String place;
    String city;
    String tag;

    public Event(String startTitle, String dateStart, String hourStart, String dateEnd, String hourEnd, String startPlace, String startCity, String startTag){

        title = startTitle;
        startDate = dateStart;
        endDate = dateEnd;
        startHour = hourStart;
        endHour = hourEnd;
        place = startPlace;
        city = startCity;
        tag = startTag;
    }

    public void setTitle (String newTitle){
        title = newTitle;
    }

    public String getTitle(){
        return title;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public String getStartHour(){
        return startHour;
    }

    public String getEndHour(){
        return endHour;
    }

    public String getPlace(){
        return place;
    }

    public String getCity(){
        return city;
    }

}
