package opensource.onlinestore.model.enums;

/**
 * Created by orbot on 09.02.16.
 */
public enum GoodsRegistryType {
    XLS(".xls"), CSV(".csv"), NONE("");

    private final String extension;

    GoodsRegistryType(String extension) {
        this.extension = extension;
    }

    public static GoodsRegistryType fromString(String extension) {
        if(extension != null) {
            for(GoodsRegistryType type : GoodsRegistryType.values()) {
                if(extension.equalsIgnoreCase(type.extension)) {
                    return type;
                }
            }
        }
        return NONE;
    }
}
