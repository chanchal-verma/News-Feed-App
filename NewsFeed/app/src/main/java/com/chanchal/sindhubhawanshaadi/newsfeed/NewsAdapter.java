package com.chanchal.sindhubhawanshaadi.newsfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0 , news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate( R.layout.list_item , parent , false);
        }

        News currentNews = getItem(position);

        TextView sectionName = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionName.setText(currentNews.getSectionName());

        TextView NewsTitle = (TextView) listItemView.findViewById(R.id.NewsTitle);
        NewsTitle.setText(currentNews.getWebTitle());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(currentNews.getWebPublicationDate());
        date.setText(formattedDate);

        TextView time = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(currentNews.getWebPublicationDate());
        time.setText(formattedTime);

        TextView type = (TextView) listItemView.findViewById(R.id.type);
        type.setText(currentNews.getType());

        return listItemView;
    }

    private String formatDate(String webPublicationDate) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");

        Date d = null;
        try
        {
            d = input.parse(webPublicationDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        return formatted;
    }

    private String formatTime(String webPublicationDate) {
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");

        Date d = null;
        try
        {
            d = input.parse(webPublicationDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        String formatted = output.format(d);
        return formatted;
    }
}
