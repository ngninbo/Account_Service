package account.domain;

import java.util.Objects;

public class AccountServiceCustomErrorMessage {

    public static String DEFAULT_MESSAGE = "User exist!";

    private String timestamp;
    private int status;
    private String error;
    private String message = DEFAULT_MESSAGE;
    private String path;

    public AccountServiceCustomErrorMessage(String timestamp, int status, String error, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.path = path;
    }

    public AccountServiceCustomErrorMessage(String timestamp, int status, String error, String message, String path) {
        this(timestamp, status, error, path);
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountServiceCustomErrorMessage)) return false;
        AccountServiceCustomErrorMessage that = (AccountServiceCustomErrorMessage) o;
        return getStatus() == that.getStatus() && Objects.equals(getTimestamp(), that.getTimestamp()) && Objects.equals(getError(), that.getError()) && Objects.equals(getMessage(), that.getMessage()) && Objects.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimestamp(), getStatus(), getError(), getMessage(), getPath());
    }

    @Override
    public String toString() {
        return "AccountServiceCustomErrorMessage{" +
                "timestamp='" + timestamp + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
