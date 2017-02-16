package com.fanlehai.java.junit.bdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

public class CucumberSteps {

    private int a;
    private int b;
    private int calculation;

    @Given("^the numbers (.*) and (.*)$")
    public void captureNumbers(final int a, final int b) {
        this.a = a;
        this.b = b;
    }

    @When("^the numbers are added together$")
    public void addNumbers() {
        this.calculation = a + b;
    }

    @When("^the first number is subtracted from the second")
    public void subtractAFromB() {
        this.calculation = b - a;
    }

    @Then("^the result is (.*)$")
    public void assertResult(final int expectedResult) {
    	Assert.assertEquals(expectedResult, calculation);
    }
}
