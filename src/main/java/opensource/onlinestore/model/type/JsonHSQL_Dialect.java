package opensource.onlinestore.model.type;

import org.hibernate.dialect.HSQLDialect;

/**
 * Created by maks(avto12@i.ua) on 05.03.2016.
 * This class is only for passing hibernate validation, json type will not working in hsqlDB!!!
 */
public class JsonHSQL_Dialect extends HSQLDialect {

    public JsonHSQL_Dialect() {
        super();
        this.registerColumnType(java.sql.Types.JAVA_OBJECT, "varchar");
    }
}
