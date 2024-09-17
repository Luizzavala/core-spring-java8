package com.core.api.infrastructure.servicesImpl;

import com.core.api.application.services.PackageService;
import com.core.api.domain.models.Package;
import com.core.api.domain.models.PackageAttributes;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class PackageServiceImpl implements PackageService {
    private final String rootPath = "com.core.api";
    private final String packageName = "com.core.api.modules";
    private String moduleName;
    private String className;
    private String tableName;
    private String classNameLowerCase;
    private Package aPackage;
    private String filters;
    private String filtersWithoutType;
    private String pk;
    private String id;

    @Override
    public void createPackage(Package packageObject) {
        {
            try {
                initializePackageData(packageObject);
                Path root = Paths.get(".").normalize().toAbsolutePath();
                Path modulePath = createModulePath(root);
                createApplication(modulePath, className);
                createDomain(modulePath, className);
                createInfrastructure(modulePath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initializePackageData(Package packageObject) {
        this.aPackage = packageObject;
        moduleName = packageObject.getModuleName();
        className = packageObject.getPackageName();
        tableName = packageObject.getTableName();
        classNameLowerCase = Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

    private Path createModulePath(Path root) {
        Path modulePath = Paths.get(root.toString(),
                "src", "main", "java", "com", "core", "api", "modules", moduleName);
        File pathModule = new File(modulePath.toString());
        if (!pathModule.exists()) {
            pathModule.mkdir();
        }
        return modulePath;
    }

    private void createApplication(Path modulePath, String className) {
        Path applicationPath = createFolder(modulePath, "application");

        Path servicesPath = createFolder(applicationPath, "services");
        if (aPackage.getAggregateRoot()) {
            Path serviceClass = createFile(servicesPath, className + "Service");
            createInterface(serviceClass, "Service");
        }
        Path statusesPath = createFolder(applicationPath, "statuses");
        if (aPackage.getAggregateRoot()) {
            char[] c = moduleName.toCharArray();
            c[0] = Character.toUpperCase(c[0]);
            String moduleName_ = new String(c);
            Path statusClass = createFile(statusesPath, moduleName_ + "Status");
            createStatus(statusClass, moduleName_);
        }
        Path validatorsPath = createFolder(applicationPath, "validators");
    }

    private void createDomain(Path modulePath, String className) {
        Path domainPath = createFolder(modulePath, "domain");
        Path modelsPath = createFolder(domainPath, "models");
        Path valueObjectPath = createFolder(modelsPath, "valueObject");
        Path modelClass = createFile(modelsPath, className);
        createModel(modelClass);

        Path portsPath = createFolder(domainPath, "ports");
        Path inPath = createFolder(portsPath, "gateway");
        if (aPackage.getAggregateRoot()) {
            Path restClass = createFile(inPath, className + "Rest");
            createInterface(restClass, "Rest");
        }
        Path outPath = createFolder(portsPath, "dataAccess");
        if (aPackage.getAggregateRoot()) {
            Path persistenceClass = createFile(outPath, className + "Persistence");
            createInterface(persistenceClass, "Persistence");
        }
    }


    private void createInfrastructure(Path modulePath) {
        Path infrastructurePath = createFolder(modulePath, "infrastructure");
        Path adaptersPath = createFolder(infrastructurePath, "adapters");
        Path inPath = createFolder(adaptersPath, "in");

        Path restPath = createFolder(inPath, "rest");
        if (aPackage.getAggregateRoot()) {
            Path controllerClass = createFile(restPath, className + "Controller");
            createController(controllerClass);
        }

        Path outPath = createFolder(adaptersPath, "out");

        Path persistencePath = createFolder(outPath, "persistence");
        Path mysqlPath = createFolder(persistencePath, "sql");

        if (aPackage.getAggregateRoot()) {
            Path mysqlPersistenceClass = createFile(mysqlPath, className + "PersistenceSql");
            createPersistenceMysql(mysqlPersistenceClass);
        }

        Path entitiesPath = createFolder(outPath, "entities");
        Path entityClass = createFile(entitiesPath, className + "Entity");
        createEntity(entityClass);

        Path valueObjectPath = createFolder(entitiesPath, "valueObject");

        Path mappersPath = createFolder(outPath, "mappers");
        Path mapperClass = createFile(mappersPath, className + "Mapper");
        createMapper(mapperClass);

        Path repositoriesPath = createFolder(outPath, "repositories");
        if (aPackage.getAggregateRoot()) {
            Path repositoryClass = createFile(repositoriesPath, className + "Repository");
            createRepository(repositoryClass);
        }

        Path servicesPath = createFolder(infrastructurePath, "servicesImpl");
        if (aPackage.getAggregateRoot()) {
            Path serviceClass = createFile(servicesPath, className + "ServiceImpl");
            createService(serviceClass);
        }
    }

    private Path createFolder(Path path, String name) {
        Path folderPath = Paths.get(path.toString(), name);
        File folderModule = new File(folderPath.toString());
        if (!folderModule.exists()) {
            folderModule.mkdir();
        }
        return folderPath;
    }

    @SneakyThrows
    private Path createFile(Path path, String name) {
        Path filePath = Paths.get(path.toString(), name + ".java");
        File fileClass = new File(filePath.toString());
        if (!fileClass.exists()) {
            fileClass.createNewFile();
        }
        return filePath;
    }

    @SneakyThrows
    private void createModel(Path modelClass) {
        FileWriter myWriter = new FileWriter(modelClass.toString());
        String packageDeclaration = "package " + packageName + "." + moduleName + ".domain.models;\n\n";
        String importStatements = "import lombok.AllArgsConstructor;\n" + "import lombok.Data;\n" + "import lombok.NoArgsConstructor;\n" + "import lombok.experimental.FieldDefaults;\n\n";
        String classNameDeclaration = "@Data\n" + "@AllArgsConstructor\n" + "@NoArgsConstructor\n" + "@FieldDefaults(level = lombok.AccessLevel.PRIVATE)\n" + "public class " + className + " {\n";
        Set<PackageAttributes> packageAttributesSet = new LinkedHashSet<>(aPackage.getPackageAttributes());
        StringBuilder fields = new StringBuilder();
        for (PackageAttributes packageAttributes : packageAttributesSet) {
            fields.append("    ").append(packageAttributes.getType()).append(" ").append(packageAttributes.getName()).append(";\n");
        }
        String classBody = "}\n";
        String classContent = packageDeclaration + importStatements + classNameDeclaration + fields.toString() + classBody;
        myWriter.write(classContent);
        myWriter.close();
    }

    @SneakyThrows
    private void createInterface(Path rest, String type) {
        FileWriter myWriter = new FileWriter(rest.toString());
        String packageName_ = "";
        if (type == "Rest") {
            packageName_ = "package " + packageName + "." + moduleName + ".domain.ports.gateway;\n\n";
            packageName_ += "import " + rootPath + ".domain.models.Response;\n";
        } else if (type == "Persistence") {
            packageName_ = "package " + packageName + "." + moduleName + ".domain.ports.dataAccess;\n\n";
            packageName_ += "import java.util.Set;\n";
        } else if (type == "Service") {
            packageName_ = "package " + packageName + "." + moduleName + ".application.services;\n\n";
            packageName_ += "import java.util.Set;\n";
        }

        packageName_ += "import " + packageName + "." + moduleName + ".domain.models." + className + ";\n\n";
        String className_ = "public interface " + className + type + " {\n";
        filters = "";
        filtersWithoutType = "";
        pk = "";
        String packageName = aPackage.getPackageName();
        String returnType = "";


        for (PackageAttributes packageAttributes : aPackage.getPackageAttributes()) {
            if (packageAttributes.getFilter()) {
                filters += packageAttributes.getType() + " " + packageAttributes.getName() + ",";
                filtersWithoutType += packageAttributes.getName() + ",";
            }
            if (packageAttributes.getPk()) {
                id = packageAttributes.getName();
                pk = packageAttributes.getType() + " " + packageAttributes.getName();
            }
        }

        StringBuilder filter_ = new StringBuilder(filters);
        filter_.deleteCharAt(filter_.length() - 1);
        filters = filter_.toString();

        StringBuilder filtersWithoutType_ = new StringBuilder(filtersWithoutType);
        filtersWithoutType_.deleteCharAt(filtersWithoutType_.length() - 1);
        filtersWithoutType = filtersWithoutType_.toString();
        if (type == "Rest") {
            returnType = "Response<?>";
            className_ += "    " + returnType + " getAll(" + filter_ + ");\n";
        } else {
            returnType = className;
            className_ += "    Set<" + returnType + "> getAll(" + filter_ + ");\n";
        }
        className_ += "    " + returnType + " getById(" + pk + ");\n";
        className_ += "    " + returnType + " save(" + aPackage.getPackageName() + " " + packageName.toLowerCase() + ");\n";
        if (type != "Persistence") {
            className_ += "    " + returnType + " enable(" + aPackage.getPackageName() + " " + packageName.toLowerCase() + ");\n";
            className_ += "    " + returnType + " disable(" + aPackage.getPackageName() + " " + packageName.toLowerCase() + ");\n";
            className_ += "    " + returnType + " delete(" + aPackage.getPackageName() + " " + packageName.toLowerCase() + ");\n";
        }
        className_ += "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createStatus(Path path, String moduleName_) {
        FileWriter myWriter = new FileWriter(path.toString());
        String packageName_ = "package " + packageName + "." + moduleName + ".application.statuses;\n\n";
        String className_ = "public class " + moduleName_ + "Status {\n";
        className_ += "    public static final Integer ENABLE = 1;\n";
        className_ += "    public static final Integer DISABLE = 2;\n";
        className_ += "    public static final Integer DELETED = 99;\n";
        className_ += "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createController(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.adapters.in.rest;\n\n";
        packageName_ += "import org.springframework.beans.factory.annotation.Autowired;\n" + "import org.springframework.http.HttpStatus;\n" + "import org.springframework.web.bind.annotation.*;\n" + "import " + rootPath + ".domain.models.Response;\n" + "import " + packageName + "." + moduleName + ".domain.models." + className + ";\n" + "import " + packageName + "." + moduleName + ".domain.ports.gateway." + className + "Rest;\n" + "import " + packageName + "." + moduleName + ".application.services." + className + "Service;\n\n" + "import java.util.HashSet;\n" + "import java.util.Set;\n\n";

        String className_ = "@RestController\n@RequestMapping()\n";
        className_ += "public class " + className + "Controller implements " + className + "Rest {\n";
        className_ += "    @Autowired\n    private " + className + "Service " + classNameLowerCase + "Service;\n\n";


        className_ += "    @Override\n";
        className_ += "    @GetMapping\n";
        className_ += "    public Response<?> getAll(\n";
        for (PackageAttributes packageAttributes : aPackage.getPackageAttributes()) {
            if (packageAttributes.getFilter()) {
                className_ += "            @RequestParam(name = \"" + packageAttributes.getName() + "\", required = false";
                if (packageAttributes.getType().equals("String")) {
                    className_ += ", defaultValue = \"\"";
                }
                className_ += ") " + packageAttributes.getType() + " " + packageAttributes.getName() + ",\n";
            }
        }
        className_ = className_.substring(0, className_.length() - 2) + "\n";
        className_ += "    ) {\n";
        className_ += "        try {\n";
        className_ += "            Set<" + className + "> " + classNameLowerCase + "s = " + classNameLowerCase + "Service.getAll(";
        for (PackageAttributes packageAttributes : aPackage.getPackageAttributes()) {
            if (packageAttributes.getFilter()) {
                className_ += packageAttributes.getName() + ", ";
            }
        }
        className_ = className_.substring(0, className_.length() - 2) + ");\n";
        className_ += "            return new Response<>( HttpStatus.OK, \"Se obtuvieron los registros correctamente.\", " + classNameLowerCase + "s);\n";
        className_ += "        } catch (Exception e) {\n";
        className_ += "            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n";
        className_ += "        }\n";
        className_ += "    }\n\n";


        className_ += "    @Override\n    @GetMapping(\"/{" + id + "}\")\n" + "    public Response<?> getById(@PathVariable " + pk + ") {\n" + "       try { \n" + "           Set<" + className + "> " + classNameLowerCase + "s = new HashSet<>();\n" + "           " + classNameLowerCase + "s.add(" + classNameLowerCase + "Service.getById(" + id + "));\n" + "           return new Response<>( HttpStatus.OK, \"Se obtuvieron los registros correctamente.\"," + classNameLowerCase + "s);\n" + "       } catch( Exception e ) { \n" + "           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n" + "       }\n" + "   }\n\n";
        className_ += "    @Override\n    @PostMapping()\n" + "    public Response<?> save(@RequestBody " + className + " " + classNameLowerCase + ") {\n" + "       try { \n" + "           Set<" + className + "> " + classNameLowerCase + "s = new HashSet<>();\n" + "           " + classNameLowerCase + "s.add(" + classNameLowerCase + "Service.save(" + classNameLowerCase + "));\n" + "           return new Response<>( HttpStatus.OK, \"Se guardó el registro correctamente.\"," + classNameLowerCase + "s);\n" + "       } catch( Exception e ) { \n" + "           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n" + "       }\n" + "   }\n\n";
        className_ += "    @Override\n    @PostMapping(\"/enable\")\n" + "    public Response<?> enable(@RequestBody " + className + " " + classNameLowerCase + ") {\n" + "       try { \n" + "           Set<" + className + "> " + classNameLowerCase + "s = new HashSet<>();\n" + "           " + classNameLowerCase + "s.add(" + classNameLowerCase + "Service.enable(" + classNameLowerCase + "));\n" + "           return new Response<>( HttpStatus.OK, \"Se Habilitó el registro correctamente.\"," + classNameLowerCase + "s);\n" + "       } catch( Exception e ) { \n" + "           return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n" + "       }\n" + "   }\n\n";
        className_ += "    @Override\n    @PostMapping(\"/disable\")\n" + "    public Response<?> disable(@RequestBody " + className + " " + classNameLowerCase + ") {\n" + "       try { \n" + "           Set<" + className + "> " + classNameLowerCase + "s = new HashSet<>();\n" + "           " + classNameLowerCase + "s.add(" + classNameLowerCase + "Service.disable(" + classNameLowerCase + "));\n" + "        return new Response<>( HttpStatus.OK, \"Se Dehabilitó el registro correctamente.\"," + classNameLowerCase + "s);\n" + "       } catch( Exception e ) { \n" + "         return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n" + "       }\n" + "   }\n\n";
        className_ += "    @Override\n    @PostMapping(\"/delete\")\n" + "    public Response<?> delete(@RequestBody " + className + " " + classNameLowerCase + ") {\n" + "       try { \n" + "           Set<" + className + "> " + classNameLowerCase + "s = new HashSet<>();\n" + "           " + classNameLowerCase + "s.add(" + classNameLowerCase + "Service.delete(" + classNameLowerCase + "));\n" + "           return new Response<>( HttpStatus.OK, \"Se Elimino el registro correctamente.\"," + classNameLowerCase + "s);\n" + "       } catch( Exception e ) { \n" + "            return new Response<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);\n" + "       }\n" + "   }\n\n";
        className_ += "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createPersistenceMysql(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        String capitalizedId = id.substring(0, 1).toUpperCase() + id.substring(1);
        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.adapters.out.persistence.sql;\n\n" + "import " + rootPath + ".infrastructure.adapters.out.persistence.sql.CriteriaAdapter;\n" + "import org.springframework.beans.factory.annotation.Autowired;\n" + "import org.springframework.stereotype.Service;\n" + "import javax.persistence.EntityManagerFactory;\n" + "import javax.persistence.criteria.Predicate;\n" + "import java.util.LinkedHashSet;\n" + "import java.util.Set;\n" + "import " + packageName + "." + moduleName + ".domain.models." + className + ";\n" + "import " + packageName + "." + moduleName + ".domain.ports.dataAccess." + className + "Persistence;\n" + "import " + packageName + "." + moduleName + ".infrastructure.adapters.out.entities." + className + "Entity;\n" + "import " + packageName + "." + moduleName + ".infrastructure.adapters.out.mappers." + className + "Mapper;\n" + "import " + packageName + "." + moduleName + ".infrastructure.adapters.out.repositories." + className + "Repository;\n\n";
        String className_ = "@Service\n" + "public class " + className + "PersistenceSql implements " + className + "Persistence {\n" + "    @Autowired\n" + "    private " + className + "Repository " + classNameLowerCase + "Repository;\n" + "    @Autowired\n" + "    private " + className + "Mapper " + classNameLowerCase + "Mapper;\n" + "    @Autowired\n" + "    private EntityManagerFactory entityManagerFactory;\n\n" + "    @Override\n" + "    public Set<" + className + "> getAll(" + filters + ") {\n" + "        CriteriaAdapter<" + className + "Entity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);\n" + "        criteriaAdapter.init(" + className + "Entity.class);\n" + "        Set<Predicate> predicates = new LinkedHashSet<>();\n" + "        criteriaAdapter.where(criteriaAdapter.and(predicates.toArray(new Predicate[0])));\n" + "        Set<" + className + "Entity> " + classNameLowerCase + "Entity = criteriaAdapter.getResultList();\n" + "        criteriaAdapter.close();\n" + "        return " + classNameLowerCase + "Mapper.toModelList(" + classNameLowerCase + "Entity);\n" + "    }\n\n" + "    @Override\n" + "    public " + className + " getById(" + pk + "){\n" + "        CriteriaAdapter<" + className + "Entity> criteriaAdapter = new CriteriaAdapter<>(entityManagerFactory);\n" + "        criteriaAdapter.init(" + className + "Entity.class);\n" + "        criteriaAdapter.where(criteriaAdapter.equal(\"" + id + "\", " + id + "));\n" + "        " + className + "Entity " + classNameLowerCase + "Entity = criteriaAdapter.getResultList().stream().findFirst().orElse(new " + className + "Entity());\n" + "        criteriaAdapter.close();\n" + "        if( " + classNameLowerCase + "Entity.get" + capitalizedId + "() != null ) {\n" + "            return " + classNameLowerCase + "Mapper.toModel(" + classNameLowerCase + "Entity);\n" + "        } else {\n" + "            return null;\n" + "        }" + "    }\n\n" + "    @Override\n" + "    public " + className + " save(" + className + " " + classNameLowerCase + "){\n" + "        " + className + "Entity " + classNameLowerCase + "Entity = " + classNameLowerCase + "Mapper.toEntity(" + classNameLowerCase + ");\n" + "        " + classNameLowerCase + "Entity = " + classNameLowerCase + "Repository.save(" + classNameLowerCase + "Entity);\n" + "        return " + classNameLowerCase + "Mapper.toModel(" + classNameLowerCase + "Entity);\n" + "    }\n\n" + "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createEntity(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.adapters.out.entities;\n\n";
        packageName_ += "import lombok.Getter;\nimport lombok.Setter;\nimport javax.persistence.*;\nimport java.io.Serializable;\n\n";
        packageName_ += "@Entity\n";
        packageName_ += "@Table(name = \"" + tableName + "\")\n";
        packageName_ += "@Getter\n";
        packageName_ += "@Setter\n";
        StringBuilder className_ = new StringBuilder("public class " + className + "Entity implements Serializable {\n");
        for (PackageAttributes packageAttributes : aPackage.getPackageAttributes()) {
            String columnName = packageAttributes.getColumnName();
            String propertyName = packageAttributes.getName();
            if (packageAttributes.isPk()) {
                className_.append("    @Id\n");
                className_.append("    @GeneratedValue(strategy = GenerationType.IDENTITY)\n");
            }
            className_.append("    @Column(name = \"").append(columnName).append("\")\n");
            className_.append("    private ").append(packageAttributes.getType()).append(" ").append(propertyName).append(";\n");
        }
        className_.append("}");
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createMapper(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        String entityName = className + "Entity";
        String entityNameLower = classNameLowerCase + "Entity";
        String entitiesNameLower = classNameLowerCase + "Entities";

        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.adapters.out.mappers;\n\n";
        packageName_ += "import " + rootPath + ".domain.ports.dataAccess.IMapper;\n" + "import " + rootPath + ".infrastructure.adapters.out.mappers.Mapper;\n" + "import " + packageName + "." + moduleName + ".domain.models." + className + ";\n" + "import " + packageName + "." + moduleName + ".infrastructure.adapters.out.entities." + className + "Entity;\n" + "import org.springframework.stereotype.Service;\n" + "import java.util.LinkedHashSet;\nimport java.util.Set;\n\n";
        String className_ = "@Service\n public class " + className + "Mapper extends Mapper<" + className + ", " + className + "Entity> " + "implements IMapper<" + className + ", " + className + "Entity> { \n";

        String toModelMethod = "    @Override\n" + "    public " + className + " toModel(" + entityName + " " + entityNameLower + ") {\n" + "        return mapperEntityToModel(" + entityNameLower + ", new " + className + "());\n" + "    }\n\n";

        String toModelListMethod = "    @Override\n" + "    public Set<" + className + "> toModelList(Set<" + entityName + "> " + entitiesNameLower + ") {\n" + "        Set<" + className + "> " + classNameLowerCase + "s = new LinkedHashSet<>();\n" + "        for( " + entityName + " " + entityNameLower + " : " + entitiesNameLower + ") {\n" + "            " + classNameLowerCase + "s.add(toModel(" + entityNameLower + "));\n" + "        }\n" + "        return " + classNameLowerCase + "s;\n" + "    }\n\n";

        String toEntityMethod = "    @Override\n" + "    public " + entityName + " toEntity(" + className + " " + classNameLowerCase + ") {\n" + "        return mapperModelToEntity(" + classNameLowerCase + ", new " + entityName + "());\n" + "    }\n";

        className_ += toModelMethod + toModelListMethod + toEntityMethod + "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createRepository(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.adapters.out.repositories;\n\n" + "import org.springframework.data.jpa.repository.JpaRepository;\n" + "import org.springframework.stereotype.Repository;\n" + "import " + packageName + "." + moduleName + ".infrastructure.adapters.out.entities." + className + "Entity;\n\n";
        String className_ = "@Repository\n" + "public interface " + className + "Repository extends JpaRepository<" + className + "Entity, Integer>{\n" + "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }

    @SneakyThrows
    private void createService(Path path) {
        FileWriter myWriter = new FileWriter(path.toString());
        char[] c = moduleName.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        String moduleName_ = new String(c);

        String packageName_ = "package " + packageName + "." + moduleName + ".infrastructure.servicesImpl;\n\n";
        packageName_ += "import org.springframework.beans.factory.annotation.Autowired;\n" + "import org.springframework.stereotype.Service;\n" + "import " + packageName + "." + moduleName + ".domain.models." + className + ";\n" + "import " + packageName + "." + moduleName + ".domain.ports.dataAccess." + className + "Persistence;\n" + "import " + packageName + "." + moduleName + ".application.statuses." + moduleName_ + "Status;\n" + "import " + packageName + "." + moduleName + ".application.services." + className + "Service;\n" + "import java.util.Set;\n\n";
        String className_ = "@Service\n" + "public class " + className + "ServiceImpl implements " + className + "Service {\n" + "    @Autowired\n    private " + className + "Persistence " + classNameLowerCase + "Persistence;\n\n" + "    @Override\n    public Set<" + className + "> getAll(" + filters + ") {\n" + "        return " + classNameLowerCase + "Persistence.getAll(" + filtersWithoutType + ");\n" + "    }\n\n" + "    @Override\n    public " + className + " getById(" + pk + ") {\n" + "        return " + classNameLowerCase + "Persistence.getById(" + id + ");\n" + "    }\n\n" + "    @Override\n    public " + className + " save(" + className + " " + classNameLowerCase + "){ \n" + "        return " + classNameLowerCase + "Persistence.save(" + classNameLowerCase + ");\n" + "    }\n\n" + "    @Override\n     public " + className + " enable(" + className + " " + classNameLowerCase + "){\n" + "        " + classNameLowerCase + ".setStatus(" + moduleName_ + "Status.ENABLE);\n" + "        return " + classNameLowerCase + "Persistence.save(" + classNameLowerCase + ");\n" + "    }\n\n" + "    @Override\n     public " + className + " disable(" + className + " " + classNameLowerCase + "){\n" + "        " + classNameLowerCase + ".setStatus(" + moduleName_ + "Status.DISABLE);\n" + "        return " + classNameLowerCase + "Persistence.save(" + classNameLowerCase + ");\n" + "    }\n\n" + "    @Override\n    public " + className + " delete(" + className + " " + classNameLowerCase + "){\n" + "        " + classNameLowerCase + ".setStatus(" + moduleName_ + "Status.DELETED);\n" + "        return " + classNameLowerCase + "Persistence.save(" + classNameLowerCase + ");\n" + "    }\n\n" + "}";
        myWriter.write(packageName_ + className_);
        myWriter.close();
    }


}
