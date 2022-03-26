package space.gavinklfong.demo.banking.services;

import org.junit.platform.suite.api.*;

@Suite
@SuiteDisplayName("JUnit Platform Suite Demo")
@SelectPackages("space.gavinklfong.demo.banking.services")
@IncludeClassNamePatterns(".*Test")
public class BankingServiceAllTests {
}
