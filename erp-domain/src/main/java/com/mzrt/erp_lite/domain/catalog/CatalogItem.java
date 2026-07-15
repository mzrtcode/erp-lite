package com.mzrt.erp_lite.domain.catalog;

import com.mzrt.erp_lite.domain.common.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class CatalogItem extends Entity<String> {

    private final String code;
    private final String value;
    private final String description;
    private final int displayOrder;
    private final Map<String, Object> metadata;
    private boolean isActive;

    public CatalogItem(
            String id,
            String code,
            String value,
            String description,
            int displayOrder,
            Map<String, Object> metadata) {

        if (code == null || code.isEmpty()){
            throw new IllegalArgumentException("Code cannot be null or empty");
        }


        super(id);
        this.code = code;
        this.value = value;
        this.description = description;
        this.displayOrder = displayOrder;
        this.metadata = metadata != null ? Map.copyOf(metadata): Map.of();
        this.isActive = true;
    }

    public Object getMetadata(String key){
        return metadata.get(key);
    }

    public boolean hasMetadata(String key){
        return metadata.containsKey(key);
    }

    public void turnOffStatus(){
        this.isActive = false;
    }

    public void turnOnStatus(){
        this.isActive = true;
    }
}
