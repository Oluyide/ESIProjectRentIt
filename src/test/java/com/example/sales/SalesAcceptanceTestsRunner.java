package com.example.sales;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by lgarcia on 2/23/2017.
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin={"pretty","html:target/cucumber"}, features = "classpath:features/sales", glue = "com.example.sales")
public class SalesAcceptanceTestsRunner {
}
