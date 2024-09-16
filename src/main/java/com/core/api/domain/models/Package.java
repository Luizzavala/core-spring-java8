package com.core.api.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Package {
    String moduleName;
    String packageName;
    Boolean aggregateRoot;
    Boolean createStatus;
    String tableName;
    List<PackageAttributes> packageAttributes;
}
