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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TypeDefs( {@TypeDef( name= "StringJsonObject", typeClass = StringJsonUserType.class)})
@Entity
@Table(name = "categories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class CategoryEntity extends BaseEntity {

    @Transient
    private static final Logger LOG = LoggerFactory.getLogger(CategoryEntity.class);

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @Type(type = "StringJsonObject")
    private String characteristicsTemplate;

    public CategoryEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public String getCharacteristicsTemplate() {
        return characteristicsTemplate;
    }

    public void setCharacteristicsTemplate(String characteristicsTemplate) {
        this.characteristicsTemplate = characteristicsTemplate;
    }

    public List<String> getCharacteristicsKeys() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> characteristicsMap =
                    mapper.readValue(characteristicsTemplate, new TypeReference<Map<String, String>>(){});
            return new ArrayList<>(characteristicsMap.keySet());
        } catch (IOException e) {
            LOG.info("Could not parse characteristics template");
            return null;
        }
    }

    public void setCharacteristicsTemplateFromList(List<String> keys) {
        ObjectMapper mapper = new ObjectMapper();
        String characteristic = "";
        try {
            Map<String, String> charachteristics = new HashMap<>();
            keys.forEach(key -> charachteristics.put(key, ""));
            characteristic = mapper.writeValueAsString(charachteristics);
        } catch (JsonProcessingException e) {
            LOG.error("Could not make json from map", e);
        }
        this.characteristicsTemplate = characteristic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
        return characteristicsTemplate != null ? characteristicsTemplate.equals(that.characteristicsTemplate) : that.characteristicsTemplate == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        result = 31 * result + (characteristicsTemplate != null ? characteristicsTemplate.hashCode() : 0);
        return result;
    }
}
