package com.example.a4reddit.Client;

import android.util.Log;

import com.example.a4reddit.data.ExtractXML;
import com.example.a4reddit.data.model.Entry;
import com.example.a4reddit.data.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by George Kimutai on 3/25/2019.
 */


@SuppressWarnings("deprecation")
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static Retrofit retrofit2;
    private static Retrofit retrofit3;
    private static Retrofit retrofit4;


    public static Retrofit getRetrofitInstance(String BASE_URL) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getInstance(String BASE_URL) {
        if (retrofit2 == null) {
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }

    public RetrofitClientInstance() {
    }

    public static Retrofit getRetrofitMySub(String BASE_URL) {
        if (retrofit3 == null) {
            retrofit3 = new Retrofit.Builder().
                    baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit3;

    }

    public static Retrofit getComments(String BASE_URL) {
        if (retrofit4 == null) {
            retrofit4 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(SimpleXmlConverterFactory.create())
                    .build();
        }
        return retrofit4;
    }


    public List<Post> fetchDataFromXML(List<Entry> entryList) {

        List<Post> postArrayList = new ArrayList<>();
        for (int x = 0; x < entryList.size(); x++) {
            String title;

            try {
                title = entryList.get(x).getCategory().getTerm();

            } catch (NullPointerException e) {
                String link = entryList.get(x).getLink().getLink();
                title = ExtractXML.takeFromLink("https://www.reddit.com/r/", link);


            }

            String fetchGropu = ExtractXML.takeFromLink("https://www.reddit.com/r/", entryList.get(x).getLink().getLink());

            //extract yT Links
            List<String> ytLink = new ArrayList<>();
            try {
                ytLink.add(ExtractXML.takeYTLink("<a href=", entryList.get(x).getContent()).get(0));
            } catch (NullPointerException e) {
                ytLink.add(null);

            } catch (IndexOutOfBoundsException e) {
                ytLink.add(null);

            }


            //extract links to website
            List<String> post = new ArrayList<>();
            try {
                post.add(ExtractXML.takePost(title, "<a href=", entryList.get(x).getContent()).get(0));
            } catch (NullPointerException e) {
                post.add(null);

            } catch (IndexOutOfBoundsException e) {
                post.add(null);

            }
            //extract links to comments
            String comments;
            try {
                comments = ExtractXML.takeComments(title, "<a href=", entryList.get(x).getContent());
            } catch (NullPointerException e) {
                comments = null;

            } catch (IndexOutOfBoundsException e) {
                comments = null;

            }


            //extract link to picture

            String thumnail;
            String TAG = "retrofit";
            try {
                thumnail = ExtractXML.testOfRightImage("<a href=", entryList.get(x).getContent());
            } catch (NullPointerException e) {

                thumnail = null;
                Log.i(TAG, "fetchDataFromXML: " + e.getMessage());

            } catch (IndexOutOfBoundsException e) {
                thumnail = null;
                Log.i(TAG, "fetchDataFromXML: " + e.getMessage());

            }
            if (thumnail == null) {
                try {
                    thumnail = ExtractXML.takeThumnail("<img src=", entryList.get(x).getContent());
                } catch (IndexOutOfBoundsException e) {
                    Log.i(TAG, "fetchDataFromXML: " + e.getMessage());
                    thumnail = null;
                }
            }
            String author;
            try {
                author = entryList.get(x).getAuthor().getName().substring(3);

            } catch (NullPointerException e) {
                author = null;
            }

            String updated;
            try {
                updated = entryList.get(x).getUpdated();

            } catch (NullPointerException e) {
                updated = null;
            }
            String formattedDate = null;
            if (updated != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
                updated = updated.substring(0, 19) + "Z";


                try {
                    Date date = simpleDateFormat.parse(updated);
                    formattedDate = date.toString();
                } catch (ParseException e) {
                    e.printStackTrace();
                    formattedDate = null;
                }
            }


            String text;
            try {
                text = ExtractXML.takeTextFromLink("<p>", entryList.get(x).getContent());

            } catch (NullPointerException e) {
                text = null;
                Log.i("tag", "fetchDataFromXML: " + e.getMessage());
            }


            postArrayList.add(new Post(entryList.get(x).getTitle(),
                    author,
                    formattedDate,
                    post,
                    thumnail,
                    comments,
                    ytLink,
                    fetchGropu,
                    text));

        }

        return postArrayList;

    }
}


