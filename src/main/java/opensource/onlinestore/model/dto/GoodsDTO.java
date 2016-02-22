package opensource.onlinestore.model.dto;

import java.util.Map;

public class GoodsDTO {
    private String article;
    private String name;
    private Double price;
    private Long count;
    private String categoryName;
    private String producer;
    private String errorDescription;
    private Map<String, String> characteristicsMap;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Map<String, String> getCharacteristicsMap() {
        return characteristicsMap;
    }

    public void setCharacteristicsMap(Map<String, String> characteristicsMap) {
        this.characteristicsMap = characteristicsMap;
    }
}
