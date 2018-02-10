package models;

/**
 * Created by amit on 25/11/17.
 */

public class YoutubeVideo {
    private String url;
    private String title;
    private String videoId;

    public YoutubeVideo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
}
