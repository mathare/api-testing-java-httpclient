package com.typicode.jsonplaceholder.steps;

import com.typicode.jsonplaceholder.helpers.RequestHelpers;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommonSteps {

    private static final String POSTS_ENDPOINT = "/posts";
    private static final String COMMENTS_ENDPOINT = "/comments";
    private static final String ALBUMS_ENDPOINT = "/albums";
    private static final String PHOTOS_ENDPOINT = "/photos";
    private static final String TODOS_ENDPOINT = "/todos";
    private static final String USERS_ENDPOINT = "/users";

    private static final Map<String, String> endpoints = Map.ofEntries(
            Map.entry("Posts", POSTS_ENDPOINT),
            Map.entry("Comments", COMMENTS_ENDPOINT),
            Map.entry("Albums", ALBUMS_ENDPOINT),
            Map.entry("Photos", PHOTOS_ENDPOINT),
            Map.entry("ToDos", TODOS_ENDPOINT),
            Map.entry("Users", USERS_ENDPOINT)
    );

    private static final String BASE_RESOURCES_DIR = "src/test/resources/";
    private static final String SCHEMAS_DIR = BASE_RESOURCES_DIR + "schemas/";
    private static final String EXPECTED_RESPONSES_DIR = BASE_RESOURCES_DIR + "expectedResponses/";

    public static HttpResponse<String> response;
    public static List<HttpResponse<String>> responses;

    @Before
    public static void setup() {
        responses = new ArrayList<>();
    }

    @When("^I make a (GET|DELETE) request to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint$")
    public static void makeRequest(String requestType, String endpoint) {
        response = requestType.equals("GET") ?
                RequestHelpers.sendGetRequestTo(endpoints.get(endpoint)) :
                RequestHelpers.sendDeleteRequestTo((endpoints.get(endpoint)));
        responses.add(response);
    }

    @When("^I make a (GET|DELETE) request to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint with a path parameter of (-?\\d+)$")
    public static void makeRequest(String requestType, String endpoint, int pathParam) {
        response = requestType.equals("GET") ?
                RequestHelpers.sendGetRequestTo(endpoints.get(endpoint) + "/" + pathParam) :
                RequestHelpers.sendDeleteRequestTo(endpoints.get(endpoint) + "/" + pathParam);
        responses.add(response);
    }

    @When("^I make a (POST|PUT) request with an empty body to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint$")
    public static void makeRequestWithEmptyBody(String requestType, String endpoint) {
        response = requestType.equals("POST") ?
                RequestHelpers.sendPostRequestTo(endpoints.get(endpoint), "{}") :
                RequestHelpers.sendPutRequestTo(endpoints.get(endpoint), "{}");
        responses.add(response);
    }

    @When("^I make a (POST|PUT) request with an empty body to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint with a path parameter of (-?\\d+)$")
    public static void makeRequestWithEmptyBody(String requestType, String endpoint, int pathParam) {
        response = requestType.equals("POST") ?
                RequestHelpers.sendPostRequestTo(endpoints.get(endpoint) + "/" + pathParam, "{}") :
                RequestHelpers.sendPutRequestTo(endpoints.get(endpoint) + "/" + pathParam, "{}");
        responses.add(response);
    }

    @When("^I make a (POST|PUT) request with the following body to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint$")
    public static void makeRequestWithBody(String requestType, String endpoint, DataTable dataTable) {
        Map<String, String> requestBodyMap = dataTable.subTable(1, 0).asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject(requestBodyMap);
        response = requestType.equals("POST") ?
                RequestHelpers.sendPostRequestTo(endpoints.get(endpoint), requestBody.toString()) :
                RequestHelpers.sendPutRequestTo(endpoints.get(endpoint), requestBody.toString());
        responses.add(response);
    }

    @When("^I make a (POST|PUT) request with the following body to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint with a path parameter of (-?\\d+)$")
    public static void makeRequestWithBody(String requestType, String endpoint, int pathParam, DataTable dataTable) {
        Map<String, String> requestBodyMap = dataTable.subTable(1, 0).asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject(requestBodyMap);
        response = requestType.equals("POST") ?
                RequestHelpers.sendPostRequestTo(endpoints.get(endpoint) + "/" + pathParam, requestBody.toString()) :
                RequestHelpers.sendPutRequestTo(endpoints.get(endpoint) + "/" + pathParam, requestBody.toString());
        responses.add(response);
    }

    @When("^I make a GET request to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint with an? \"(.*)\" query parameter of (.*)$")
    public static void makeGetRequestWithQueryParameter(String endpoint, String key, String value) {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        response = RequestHelpers.sendGetRequestTo(endpoints.get(endpoint) + RequestHelpers.buildQueryParamsString(params));
        responses.add(response);
    }

    @When("^I make a GET request to the (Posts|Comments|Albums|Photos|ToDos|Users) endpoint with nested path parameters of (-?\\d+\\/\\w+)$")
    public static void makeRequestWithNestedParameters(String endpoint, String nestedParam) {
        response = RequestHelpers.sendGetRequestTo(endpoints.get(endpoint) + "/" + nestedParam);
        responses.add(response);
    }

    @Then("the response has a status code of {int}")
    public static void verifyResponseStatusCode(int code) {
        assertEquals(code, response.statusCode());
    }

    @Then("the response body follows the {string} JSON schema")
    public static void verifyResponseBodyAgainstJsonSchema(String type) throws IOException {
        String filename = SCHEMAS_DIR + type.replaceAll(" ", "") + "Schema.json";
        String json = Files.readString(new File(filename).toPath());
        JSONObject schemaObject = new JSONObject(json);
        Schema expectedSchema = SchemaLoader.load(schemaObject);
        if (response.body().startsWith("[")) {
            expectedSchema.validate(new JSONArray(response.body()));
        } else {
            expectedSchema.validate(new JSONObject(response.body()));
        }
    }

    @Then("the results array contains {int} elements")
    public static void verifyNumberOfResultsArrayElements(int numElements) {
        JSONArray results = new JSONArray(response.body());
        assertEquals(numElements, results.length());
    }

    @Then("the response body matches the {string} expected response")
    public static void verifyResponseBodyAgainstExpectedResponse(String expectedResponse) throws IOException {
        String filename = EXPECTED_RESPONSES_DIR + expectedResponse.replaceAll(" ", "") + "Response.json";
        String json = Files.readString(new File(filename).toPath());
        assertEquals(json.replace("\r", ""), response.body());
    }

    @Then("^the response body matches the (\\d+).{2} (?:post|comment|album|todo|user) in the \"(.*)\" expected response$")
    public static void verifyResponseBodyAgainstPartOfExpectedResponse(int index, String expectedResponse) throws IOException {
        String filename = EXPECTED_RESPONSES_DIR + expectedResponse.replaceAll(" ", "") + "Response.json";
        String json = Files.readString(new File(filename).toPath());
        JSONObject expected = new JSONArray(json).getJSONObject(index - 1);
        JSONObject actual = new JSONObject(response.body());
        assertEquals(expected.toString(), actual.toString());
    }

    @Then("the response body matches the following")
    public static void verifyResponseBodyAgainstDataTable(DataTable dataTable) {
        Map<String, String> expectedBody = dataTable.subTable(1, 0).asMap(String.class, String.class);
        JSONObject actual = new JSONObject(response.body());
        assertEquals(expectedBody.keySet(), actual.keySet());
        expectedBody.forEach((k, v) -> assertEquals(expectedBody.get(k), actual.get(k).toString()));
    }

    @Then("the response body is an empty JSON object")
    public static void verifyResponseBodyIsEmptyJSONObject() {
        assertEquals(new JSONObject().toString(), response.body());
    }

    @Then("the two response bodies are identical")
    public static void verifyResponseBodiesMatch() {
        String[] responseBodies = {responses.get(responses.size() - 2).body(), responses.get(responses.size() - 1).body()};
        for (int i = 0; i < responseBodies.length; i++) {
            if (responseBodies[i].startsWith("[")) {
                responseBodies[i] = new JSONArray(responseBodies[i]).get(0).toString();
            } else {
                responseBodies[i] = new JSONObject((responseBodies[i])).toString();
            }
        }
        assertEquals(responseBodies[0], responseBodies[1]);
    }
}
