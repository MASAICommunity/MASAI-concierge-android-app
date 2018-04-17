package solutions.masai.masai.android.core.model;

/**
 * Created by Semko on 2017-03-03.
 */

public class GoogleMapsAddress {
    private Result[] results;

    public Result[] getResults() {
        return results;
    }

    public void setResults(Result[] results) {
        this.results = results;
    }

    public class Result {
        private String formatted_address;

        public String getFormattedAddress() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }
    }
}
