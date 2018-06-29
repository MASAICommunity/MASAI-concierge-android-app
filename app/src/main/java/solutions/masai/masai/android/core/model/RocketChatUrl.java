package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-05-08.
 */

public class RocketChatUrl {
    private String url;
    private Meta meta;
    private ParsedUrl parsedUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public ParsedUrl getParsedUrl() {
        return parsedUrl;
    }

    public void setParsedUrl(ParsedUrl parsedUrl) {
        this.parsedUrl = parsedUrl;
    }

    public class Meta {
        private String pageTitle;
        private String ogImage;
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getOgImage() {
            return ogImage;
        }

        public void setOgImage(String ogImage) {
            this.ogImage = ogImage;
        }
    }

    public class ParsedUrl {
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
}
