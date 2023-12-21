package proj1.vttp.pokemon.model;

public class Article {
    private String title;
    private String imageUrl;
    private String description;
    private String timestamp;
    public String articleUrl;

    public Article() {
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title, String string) {
        this.title = title;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public String getArticleUrl() {
        return articleUrl;
    }
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    
    
}
