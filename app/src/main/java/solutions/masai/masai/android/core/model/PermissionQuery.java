package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-04-20.
 */

public class PermissionQuery {
    private String url;
    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public class Payload {
        private String[] scope;

        public String[] getScope() {
            return scope;
        }

        public void setScope(String[] scope) {
            this.scope = scope;
        }
    }
}
