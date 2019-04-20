package com.example.a4reddit.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George Kimutai on 3/28/2019.
 */


public class ExtractXML {
    private static final String TAG = "ExtractXML";
    private String tag;
    private String xml;
    private static final String BASE = "https://www.reddit.com/r/";
    private static final String SecondPart = "/comments/";
    private static final int COUNT_BAS = BASE.length();
    private static final int COUNT_SEC = SecondPart.length();

    public ExtractXML(String title, String tag, String xml) {
        this.tag = tag;
        String title1 = title;
        this.xml = xml;
    }

    public ExtractXML(String tag, String xml) {
        this.tag = tag;
        this.xml = xml;
    }

    public static List<String> takePost(String title, String tag, String xml) {
        List<String> post = new ArrayList<>();
        String[] splitXML = xml.split(tag + "\"");

        int count = splitXML.length;

        for (int x = 1; x < count; x++) {
            String temp = splitXML[x];
            String sub = temp.substring(0, 27);

            int countOfCharInTitle = title.length();
            String comment = temp.substring(0, COUNT_BAS + countOfCharInTitle + COUNT_SEC);
            String firstOption = temp.substring(0, 17);
            String secondOption = temp.substring(0, 24);
            String thirdOption = temp.substring(0, 22);


            if (!sub.equals("https://www.reddit.com/user") && !comment.equals(BASE + title + SecondPart) && !firstOption.equals("https://youtu.be/")
                    && !secondOption.equals("https://www.youtube.com/") && !thirdOption.equals("https://m.youtube.com/")) {
                int index = temp.indexOf("\"");
                temp = temp.substring(0, index);
                post.add(temp);

            }
        }
        return post;
    }

    public static String takeThumnail(String tag, String xml) {
        String photo = null;
        String[] splitPhoto = xml.split(tag + "\"");
        int count = splitPhoto.length;

        for (int x = 1; x < count; x++) {
            String onePhoto = splitPhoto[x];
            int index = onePhoto.indexOf("\"");
            onePhoto = onePhoto.substring(0, index);
            photo = onePhoto;
        }
        return photo;
    }

    public static String testOfRightImage(String tag, String xml) {
        String photo = null;
        String[] splitXML = xml.split(tag + "\"");
        int count = splitXML.length;
        for (int x = 1; x < count; x++) {
            String temp = splitXML[x];
            int index = temp.indexOf("\"");
            String link = temp.substring(0, index);
            int countOfLink = link.length();
            String last3Letter = link.substring(countOfLink - 4, countOfLink - 1);
//||last3Letter.equals("gif")
            if (last3Letter.equals(".jp")) {
                photo = link;


            }

        }

        return photo;
    }

    public static List<String> takeYTLink(String tag, String xml) {

        List<String> listOfLinkYT = new ArrayList<>();
        String[] splitYT = xml.split(tag + "\"");
        int countOfSplitComm = splitYT.length;
        if (countOfSplitComm > 0) {
            for (int x = 1; x < countOfSplitComm; x++) {
                String oneLiene = splitYT[x];
                String firstOption = oneLiene.substring(0, 17);
                String secondOption = oneLiene.substring(0, 24);
                String thirdOption = oneLiene.substring(0, 22);
                if (firstOption.equals("https://youtu.be/")) {
                    int index = oneLiene.indexOf("\"");
                    oneLiene = oneLiene.substring(0, index);
                    listOfLinkYT.add(oneLiene);
                } else if (secondOption.equals("https://www.youtube.com/")) {
                    int index = oneLiene.indexOf("\"");
                    oneLiene = oneLiene.substring(0, index);
                    listOfLinkYT.add(oneLiene);
                } else if (thirdOption.equals("https://m.youtube.com/")) {
                    int index = oneLiene.indexOf("\"");
                    oneLiene = oneLiene.substring(0, index);
                    listOfLinkYT.add(oneLiene);


                }
            }
        }
        return listOfLinkYT;
    }

    public static String takeComments(String title, String tag, String xml) {
        String Comment = null;
        String[] splitComm = xml.split(tag + "\"");
        int countOfSplitComm = splitComm.length;
        if (countOfSplitComm > 0) {
            for (int x = 1; x < countOfSplitComm; x++) {
                String oneLiene = splitComm[x];
                int countOfCharInTitle = title.length();
                String comment = oneLiene.substring(0, COUNT_BAS + countOfCharInTitle + COUNT_SEC);

                if (comment.equals(BASE + title + SecondPart)) {

                    int index = oneLiene.indexOf("\"");
                    oneLiene = oneLiene.substring(0, index);
                    Comment = oneLiene;
                }
            }
        }
        return Comment;
    }

    public static String takeFromLink(String tag, String xml) {
        String title = null;
        String[] splitTitle = xml.split(tag);
        int count = splitTitle.length;
        for (int x = 1; x < count; x++) {
            String takeTitle = splitTitle[x];
            int takeIndex = takeTitle.indexOf("/");
            title = takeTitle.substring(0, takeIndex);
        }
        return title;
    }

    // here I need to delete #39; and <strong></strong>
    public static String takeTextFromLink(String tag, String xml) {
        StringBuilder title = new StringBuilder();
        String[] splitTitle = xml.split(tag);
        int count = splitTitle.length;
        for (int x = 1; x < count; x++) {
            String takeTitle = splitTitle[x];
            int takeIndex = takeTitle.indexOf("</p>") + 4;
            String check = takeTitle.substring(0, takeIndex);
            String cuttingTitle = check.substring(check.length() - 4, check.length());
            if (cuttingTitle.equals("</p>")) {
                String newTitle = check.substring(0, check.length() - 4);
                String[] splitWeb = newTitle.split("<a href=");
                int countOfWebToSplit = splitWeb.length;
                if (countOfWebToSplit > 1) {
                    for (int z = 1; z < countOfWebToSplit; z++) {
                        String web = splitWeb[z];
                        int last = web.indexOf(">") + 1;
                        int lastA = web.lastIndexOf("</a>") + 4;
                        String checkA = web.substring(last, lastA);
                        String cuttingA = checkA.substring(checkA.length() - 4, checkA.length());
                        if (cuttingA.equals("</a>")) {
                            String cu = checkA.substring(0, checkA.length() - 4);
                            title.append(" ").append(cu);
                        }
                    }
                } else {
                    title.append(newTitle);
                }


            }
        }

        return title.toString().replaceAll("&#39;", "'");
    }
}


