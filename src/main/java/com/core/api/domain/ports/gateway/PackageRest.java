package com.core.api.domain.ports.gateway;

import com.core.api.domain.models.Response;
import com.core.api.domain.models.Package;

public interface PackageRest {
    Response<?> createPackage(Package packageObject);
}
