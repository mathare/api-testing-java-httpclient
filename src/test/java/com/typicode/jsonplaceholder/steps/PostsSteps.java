package com.typicode.jsonplaceholder.steps;

import io.cucumber.java.en.Then;
import org.json.JSONObject;

import static com.typicode.jsonplaceholder.steps.CommonSteps.response;
import static org.junit.Assert.assertEquals;

public class PostsSteps {

    @Then("^the \"(id|userId)\" field in the response body has a value of (\\d+)$")
    public static void verifyResponseFieldValue(String field, int value) {
        JSONObject actual = new JSONObject(response.body());
        assertEquals(value, actual.get(field));
    }

    @Then("the {string} field in the response body has a value of {string}")
    public static void verifyResponseFieldValue(String field, String value) {
        JSONObject actual = new JSONObject(response.body());
        assertEquals(value, actual.get(field));
    }

    @Then("the {string} field in the response body has a value of")
    public static void verifyResponseFieldDocstring(String field, String value) {
        JSONObject actual = new JSONObject(response.body());
        assertEquals(value, actual.get(field));
    }
}
