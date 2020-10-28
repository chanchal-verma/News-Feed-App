package com.chanchal.sindhubhawanshaadi.newsfeed;

public class News {

    private String sectionName ;

    private String webTitle;

    private String webUrl;

    private String webPublicationDate ;

    private String Type;

    public  News(String sectionName , String webTitle , String webUrl , String webPublicationDate , String Type )
    {
        this.sectionName = sectionName;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.webPublicationDate = webPublicationDate;
        this.Type = Type;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public String getType() {
        return Type;
    }
}
