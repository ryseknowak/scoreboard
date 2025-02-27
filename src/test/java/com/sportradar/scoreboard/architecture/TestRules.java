package com.sportradar.scoreboard.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;

@AnalyzeClasses(packages = "com.sportradar.scoreboard")
class TestRules {

    @ArchTest
    static final ArchRule TEST_CLASSES_SHOULD_BE_IN_SAME_PACKAGE =
            GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation();

}
