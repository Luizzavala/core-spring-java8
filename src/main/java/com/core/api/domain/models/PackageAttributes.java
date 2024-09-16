package com.core.api.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PackageAttributes implements Comparable<PackageAttributes> {
    Integer id;
    String name;
    String columnName;
    String type;
    Boolean pk;
    Boolean filter;

    @Override
    public int compareTo(PackageAttributes u) {
        if (getId() == null || u.getId() == null) {
            return 0;
        }
        return getId().compareTo(u.getId());
    }

    public boolean isPk() {
        return pk;
    }
}
