package opensource.onlinestore.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import opensource.onlinestore.model.type.StringJsonUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
@Entity
@Table(name = "goods")
public class GoodsEntity extends BaseEntity {

    @Transient
    private static final Logger LOG = LoggerFactory.getLogger(GoodsEntity.class);

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "article", nullable = false)
    private String article;

    @NotNull
    @Column(name = "counts", nullable = false)
    private Long counts;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "producer", nullable = false)
    private String producer;

    @Type(type = "StringJsonObject")
    private String characteristics;

    //TODO ?????
    @Transient
    private byte[] image;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "goods_messages",
            joinColumns = @JoinColumn(name = "goods_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "messages_id", referencedColumnName = "id"))
    private List<MessageEntity> opinions;

    public GoodsEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Long getCounts() {
        return counts;
    }

    public void setCounts(Long counts) {
        this.counts = counts;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<MessageEntity> getOpinions() {
        return opinions;
    }

    public void setOpinions(List<MessageEntity> opinions) {
        this.opinions = opinions;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public Map<String,String> getCharachteristicsAsMap() {
        if(characteristics == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> characteristic = null;
        try {
            characteristic = mapper.readValue(characteristics, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            LOG.error("Could not parse json", e);
        }
        return characteristic;
    }

    public void setCharachteristicsFromMap(Map<String, String> charachteristics) {
        ObjectMapper mapper = new ObjectMapper();
        String characteristic = "";
        try {
            characteristic = mapper.writeValueAsString(charachteristics);
        } catch (JsonProcessingException e) {
            LOG.error("Could not make json from map", e);
        }
        this.characteristics = characteristic;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsEntity that = (GoodsEntity) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (article != null ? !article.equals(that.article) : that.article != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (producer != null ? !producer.equals(that.producer) : that.producer != null) return false;
        return Arrays.equals(image, that.image);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (article != null ? article.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (producer != null ? producer.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(image);
        result = 31 * result + (opinions != null ? opinions.hashCode() : 0);
        return result;
    }
}
