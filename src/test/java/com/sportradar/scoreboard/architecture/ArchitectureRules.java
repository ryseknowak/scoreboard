package com.sportradar.scoreboard.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "com.sportradar.scoreboard", importOptions = {ImportOption.DoNotIncludeTests.class})
public class ArchitectureRules {

    private static final String INCOMING_PORTS = "..ports.incoming..";
    private static final String OUTGOING_PORTS = "..ports.outgoing..";

    @ArchTest
    static final ArchRule ONLY_INTERFACES_RESIDES_IN_INCOMING_PORTS =
            classes().that().resideInAPackage(INCOMING_PORTS)
                    .should().beInterfaces();

    @ArchTest
    static final ArchRule ONLY_INTERFACES_RESIDES_IN_OUTGOING_PORTS =
            classes().that().resideInAPackage(OUTGOING_PORTS)
                    .should().beInterfaces();

    @ArchTest
    static final ArchRule CORE_INCOMING_PORTS_SHOULD_ONLY_BE_ACCESSED_BY_ADAPTERS =
            classes().that().resideInAPackage(INCOMING_PORTS)
                    .should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..adapters..adapter..");

    @ArchTest
    static final ArchRule CORE_OUTGOING_PORTS_SHOULD_ONLY_BE_ACCESSED_BY_ADAPTERS =
            classes().that().resideOutsideOfPackages("..adapters..adapter..").should().notBeAssignableTo(OUTGOING_PORTS);

    @ArchTest
    static final ArchRule ENTITIES_ARE_ONLY_USED_IN_DATA =
            ArchRuleDefinition.noClasses()
                    .that().resideOutsideOfPackages("..adapters.data..")
                    .should().accessClassesThat().haveSimpleNameEndingWith("Entity");

    @ArchTest
    static final ArchRule DTOS_RESIDE_IN_PORTS =
            classes().that().haveSimpleNameEndingWith("Dto")
                    .should().resideInAnyPackage("..ports..dto..");

    @ArchTest
    static final ArchRule NO_CYCLES =
            slices().matching("com.sportradar.scoreboard..(*).(*).(*)")
                    .should().beFreeOfCycles();
}
