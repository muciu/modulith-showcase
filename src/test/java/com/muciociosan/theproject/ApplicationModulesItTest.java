package com.muciociosan.theproject;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SpringBootTest
@DisplayName("Application modules")
class ApplicationModulesItTest {

    @Test
    @DisplayName("should not allow illegal dependencies between modules")
    void shouldNotAllowIllegalDependenciesBetweenModules() {
        ApplicationModules.of(TheprojectApplication.class).verify();
    }

    @Test
    @DisplayName("should allow access to openapi generated package only from rest adapters and GlobalExceptionHandler")
    void shouldAllowAccessToOpenapiGeneratedPackageOnlyFromRestAdapters() {
        final var importedClasses = new ClassFileImporter()
            .withImportOption(new ImportOption.DoNotIncludeTests())
            .importPackages("com.muciociosan.theproject");

        noClasses()
            .that().resideOutsideOfPackages("com.muciociosan.theproject.applicationlogic..rest", "com.muciociosan.theproject.openapi.generated..")
            .and().doNotHaveFullyQualifiedName("com.muciociosan.theproject.infrastructure.ApplicationStatusController")
            .and().doNotHaveFullyQualifiedName("com.muciociosan.theproject.infrastructure.FakeMailSenderController")
            .and().doNotHaveFullyQualifiedName("com.muciociosan.theproject.infrastructure.GlobalExceptionHandler")
            .should().dependOnClassesThat().resideInAnyPackage("com.muciociosan.theproject.openapi..")
            .check(importedClasses);
    }
}
