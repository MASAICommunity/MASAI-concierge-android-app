package solutions.masai.masai.android.core.model;

public class MessageAttachment {
    private String title;
    private String title_url;
    private String title_link;
    private String image_url;
    private String image_type;
    private String image_size;

    public String getImageSize() {
        return image_size;
    }

    public String getImageType() {
        return image_type;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getTitleUrl() {
        return title_url;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleLink() {
        return title_link;
    }
}
