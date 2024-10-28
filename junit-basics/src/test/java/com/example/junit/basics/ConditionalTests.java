package com.example.junit.basics;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;


class ConditionalTests {
    @Test
    @Disabled("Needs fix on JIRA #1151")
    void basicTest() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testForWindows() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnOs({OS.MAC, OS.LINUX})
    void testForMacAndLinux() {
        // execute method and perform asserts
    }

    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testForJava8() {
        // execute method and perform asserts
    }

    @Test
    @EnabledForJreRange(min=JRE.JAVA_17, max=JRE.JAVA_22)
    void testForJava17And21() {
        // execute method and perform asserts
    }

    @Test
    @EnabledIfEnvironmentVariable(named="JUNIT_ENV", matches="DEV")
    void testForDevEnv() {
        // execute method and perform asserts
    }

    @Test
    @EnabledIfSystemProperty(named="JUNIT_SYS_PROP", matches="CI_CD")
    void testForSystemProperty() {
        // execute method and perform asserts
    }
}
