package myebay.store.model;

public class Seller {
    String username;
    float feedbackPercentage;
    long feedbackScore;

    public Seller(String username, float feedbackPercentage, long feedbackScore) {
        this.username = username;
        this.feedbackPercentage = feedbackPercentage;
        this.feedbackScore = feedbackScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getFeedbackPercentage() {
        return feedbackPercentage;
    }

    public void setFeedbackPercentage(float feedbackPercentage) {
        this.feedbackPercentage = feedbackPercentage;
    }

    public long getFeedbackScore() {
        return feedbackScore;
    }

    public void setFeedbackScore(long feedbackScore) {
        this.feedbackScore = feedbackScore;
    }
}
