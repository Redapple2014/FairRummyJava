package org.fcesur.gcs.constants;

public final class Constants {
    public static final String TEMPLATE_COLLECTION = "template";
    public static final int POLL_INTERVAL_IN_MS_DEFAULT = 2000;
    public static final String KAFKA_TOPIC_FOR_MATCHMAKER = "matchMaker";

    public static final String HTTP_REQ_TYPE = "requestType";
    public static final String HTTP_BODY = "httpBody";
    public static final String HTTP_HEADER_SERVICE_NAME = "servicename";
    public static final String HTTP_HEADER_OPERATION_NAME = "opname";
    public static final String IS_RETRY = "isRetry";
    public static final String API_TRANSACTION_ID = "uniqueAPIIdentifier";

    public enum MatchMakingAttribute {
        SKILL("skill");

        private String type;

        private MatchMakingAttribute(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum Distribution {
        MAX("max"), EVEN("even");

        private String type;

        private Distribution(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum Communication {
        PUSH("push"),
        PULL("pull");
        private String type;

        private Communication(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

}
