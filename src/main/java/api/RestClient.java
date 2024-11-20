package api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.RestAssuredResponseImpl;
import io.restassured.internal.log.LogRepository;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RestClient {

    private String cookie;
    private String apiToken;
    private String token;
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String contentTypeOverride;
    private boolean validationEnabled = true;
    private boolean refreshLoginEnabled = false;
    private ContentTypeSwitcher contentTypeSwitcher;
    private RequestSpecification requestSpec;
    private String perPage;
    private int sessionRetries;
    private String errorString = System.getProperty("error.string");
    private StatusCodeHandler statusCodeHandler;
    private static final String COOKIE_KEY = "FKLEUSGS";
    private static final String JSON_APP_TYPE = "application/json";
    private static final String API_TOKEN = "API_TOKEN";
    private static final String BASE_URL = "BASE_URL";
    private static final String LOGIN_URL = System.getProperty("LOGIN_URL");
    private static final String TOKEN_URL = System.getProperty("TOKEN_URL");
    private static final String STATUS_CODE_ERROR_MESSAGE = "Status code mismatch";
    private static final Logger LOGGER = LoggerFactory.getLogger(RestClient.class);
    private static final String TRACE_MESAGE = "{} Request Completed to {} - Elapsed Time {} millisecond.";

    public RestClient () {
        MatcherAssert.assertThat("No value set for System property BaseUrl", System.getProperty("BASE_URL"), Is.is(CoreMatchers.notNullValue()));
        initializeStaticRestAssured(System.getProperty("BASE_URL"));
        this.contentTypeSwitcher = new FrameworkContentTypeSwitcher();
        String retryProperty = System.getProperty("session.retries");
        this.sessionRetries = retryProperty != null ? Integer.parseInt(retryProperty) : 4;
        this.statusCodeHandler = new StatusCodeHandler();
        this.requestSpec = this.getNonLoggedInRequestSpec();
    }

    public void passwordLogin (String username, String password) {
        MatcherAssert.assertThat("No value set for System property Login url", LOGIN_URL, Is.is(CoreMatchers.notNullValue()));
        Response response = (Response) RestAssured.given().param("username", new Object[]{username}).param("password", new Object[]{password}).when().post(LOGIN_URL, new Object[0]);
        LOGGER.trace("{} Request Completed to: {} - Elapsed Time: {} millisecond.", new Object[]{Method.POST, TOKEN_URL, response.time()});
        ((ValidatableResponse)((ValidatableResponse)response.then()).assertThat()).statusCode(302);
        this.username = username;
        this.password = password;
        this.cookie = response.getCookie("GSDAGTEDG");
        this.apiToken = response.getHeader("API-TOKEN");
        this.requestSpec = this.getBasicRequestSpec();
    }

    public Response get(String endPoint) {
        this.handleContentType(endPoint);
        return this.getIt(this.addPerPage(endPoint));
    }

    public Response get(String endPoint, String id) {
        this.handleContentType(endPoint);
        return this.getIt(endPoint + "/"+ id);
    }

    public Response get(String endPoint, int id) {
        this.handleContentType(endPoint);
        return this.getIt(endPoint + "/"+ id);
    }

    public Response get(String endPoint, Map<String, Object> param) {
        this.handleContentType(endPoint);
        return this.getIt(endPoint, param);
    }

    public List<Response> getAll (String endPoint) {
        this.handleContentType(endpoint);
        List<Response> result = new ArrayList<>();
        String perPageEndPoint = this.addPerPage(endPoint);
        result.add(this.getIt(perPageEndPoint));
        String pageCount = ((Response)result.get(0)).header("X-Paging-PageCount");
        if (pageCount != null && !pageCount.isEmpty()) {
            Integer count = Integer.valueOf(pageCount);
            if(count != null && count > 1) {
                for(int i = 2; i<count+1; i++) {
                    result.add(this.geIt(perPageEndPoint + "&page=" + i));
                }
            }
        }
        return result;
    }

    private Response getIt (String endPoint) {
        return this.getIt(endPoint, (Map)null);
    }

    private Response getIt (String endPoint, Map<String, Object> param) {
        int retries = 0;
        Response response;

        do {
            ++retries;
            RequestSpecification requestSpecification1 = RestAssured.given().spec(this.requestSpec);
            if(param != null) {
                requestSpecification1.queryParams(param);
            }

            response = (Response)requestSpecification1.get(endPoint, new Object[0]);
            LOGGER.trace("{} Request Completed to : {} - Elapsed Time: {} millisecond.", new Object[]{Method.GET, endPoint, response.time()});
        } while(this.isSessionInvalid(response, retries));
        this.handleSatusCodeValidation(Method.GET, response);
        return response;
    }

    public Response post(String endPoint, String body) {
        return this.post(endPoint, body, (Map)null);
    }

    public Response post(String endPoint, Map<String, Object> params) {
        return this.post(endPoint, (String)null, params);
    }

    public Response post(String endPoint, String body, Map<String, Object> param) {
        this.handleContentType(endPoint);
        int retries = 0;

        Response response;

        do {
            ++retries;
            RequestSpecification requestSpecification1 = RestAssured.given().spec(this.requestSpec);
            if(param != null) {
                requestSpecification1.queryParams(param);
            }

            if(body != null) {
                requestSpecification1.queryParams(param);
            }

            response = (Response)requestSpecification1.post(endPoint, new Object[0]);
            LOGGER.trace("{} Request Completed to : {} - Elapsed Time: {} millisecond.", new Object[]{Method.POST, endPoint, response.time()});
        } while(this.isSessionInvalid(response, retries));

        this.handleSatusCodeValidation(Method.POST, response);
        this.requestSpec.contentType("application/json");
        return response;
    }

    public Response put(String endPoint, String id, String body) {
        this.handleContentType(endPoint);
        return this.putIt(endPoint + "/" + id, body);
    }

    public Response put(String endPoint, int id, String body) {
        this.handleContentType(endPoint);
        return this.putIt(endPoint + "/" + id, body);
    }

    public Response put(String endPoint, String body) {
        this.handleContentType(endPoint);
        return this.putIt(endPoint, body);
    }

    public Response putIt(String endPoint, String body) {
        int retries = 0;

        Response response;

        do {
            ++retries;
            response = (Response)RestAssured.given().spec(this.requestSpec).body(body).put(endPoint, new Object[0]);
            LOGGER.trace("{} Request Completed to : {} - Elapsed Time: {} millisecond.", new Object[]{Method.PUT, endPoint, response.time()});
        } while(this.isSessionInvalid(response, retries));

        this.handleSatusCodeValidation(Method.POST, response);
        this.requestSpec.contentType("application/json");
        return response;
    }

    public Response delete(String endpoint) {
        return this.deleteIt(endpoint, (String)null, (String)null);
    }

    public Response delete(String endpoint, int id) {
        return this.deleteIt(endpoint, String.valueOf(id), (String)null);
    }

    public Response delete(String endpoint, int id, String body) {
        return this.deleteIt(endpoint, String.valueOf(id), body);
    }

    public Response delete(String endpoint, String id) {
        return id != null && !id.isEmpty() ? this.deleteIt(endpoint, id, (String)null) : this.deleteIt(endpoint, (String)null, (String)null);
    }

    public Response deleteIt (String endPoint, String i, String body) {
        this.handleContentType(endPoint);
        String deleteEndPoint = id == null ? endPoint : endPoint + "/" + id;
        int retries = 0;

        Response response;

        do {
            ++retries;
            RequestSpecification requestSpecification1 = RestAssured.given().spec(this.requestSpec);

            if(body != null) {
                requestSpecification1 = requestSpecification1.body(body);
            }

            response = (Response)requestSpecification1.delete(endPoint, new Object[0]);
            LOGGER.trace("{} Request Completed to : {} - Elapsed Time: {} millisecond.", new Object[]{Method.DELETE, endPoint, response.time()});
        } while(this.isSessionInvalid(response, retries));

        this.handleSatusCodeValidation(Method.POST, response);
        return response;
    }


    public Response postMultipartFile (String endPoint, MultiPartSpecification multiPartSpecification) {
        int retries = 0;
        Response response;
        do {
            ++retries;
            response = (Response) RestAssured.given().spec(this.requestSpec).contentType("multipart/form-data").multiPart(multiPartSpecification).post(endPoint, new Object[0]);
            LOGGER.trace("{} Request Completed to : {} - Elapsed Time: {} millisecond.", new Object[]{Method.POST, endPoint, response.time()});
        } while(this.isSessionInvalid(response, retries));

        this.handleSatusCodeValidation(Method.POST, response);
        return response;
    }

    public RequestSpecification addCustomerHeaderRequestSpec (String headerName, String headerValue) {
        return (new RequestSpecBuilder()).setContentType("application/json").addHeader("API-TOKEN", this.apiToken).addHeader(headerName, headerValue)
                .addCookie("GHJGHFGHF", this.cookie, new Object[0]).build();
    }

    public String getPerPage () {
        return this.perPage;
    }

    public void setPerPage (String perPage) {
        this.perPage = perPage;
    }

    public void setBaseUrl (String url) {
        RestAssured.reset();
        initializeStaticRestAssured(url);
        this.requestSpec = this.getNonLoggedInRequestSpec();
    }

    private RequestSpecification getBasicRequestSpect () {
        return (new RequestSpecBuilder()).setContentType("application/json").addHeader("API-TOKEN", this.apiToken)
                .addCookie("GHJGHFGHF", this.cookie, new Object[0]).build();
    }

    private RequestSpecification getAuthRequestSpec () {
        return (new RequestSpecBuilder()).setContentType("application/json")
                .addHeader("Authorization", "Bearer" + this.apiToken)
                .build();
    }

    private RequestSpecification getNonLoggedInRequestSpec () {
        return (new RequestSpecBuilder()).setContentType("application/json").build();
    }

    private static void initializeStaticRestAssured (String url) {
        RestAssured.baseURI = url;
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.defaultParser = Parser.JSON;
    }

    private String addPerPage (String url) {
        String result;
        if (url.contains(("per_page"))) {
            result = url;
        } else {
            String removeAmpersand = url.endsWith("&") ? url.substring(0, url.length()-1) : url;
            String removeQuestionMark = removeAmpersand.endsWith("?") ? removeAmpersand.substring(0, removeAmpersand.length()-1) : removeAmpersand;
            result = removeQuestionMark + (removeQuestionMark.contains("?") ? "&": "?") + "per_page=" + this.perPage;
        }

        return  result;
    }

    private void handleContentType (String endpoint) {
        String type = this.contentTypeOverride == null ? this.contentTypeSwitcher.determineContentType(endpoint) : this.consumeContentTypeOverride();
        this.requestSpec.contentType(type);
        if(type.equals("application/json")) {
            this.requestSpec.accept(ContentType.fromContentType(type));
        }
        if (type.equals(String.valueOf(ContentType.ANY))) {
            this.requestSpec.accept(ContentType.ANY);
        }
    }

    private String consumeContentTypeOverride() {
        String result = this.contentTypeOverride;
        this.contentTypeOverride = null;
        return result;
    }

    public void setContentTypeOverride(String contentType) {
        this.contentTypeOverride = contentType;
    }

    private void handleSatusCodeValidation(Method method, Response response) {
        if(this.validationEnabled) {
            this.logIfError(response);
            this.checkForErrorString(response);
            LogRepository requestLog = ((RestAssuredResponseImpl)response).getLogRepository();
            if(this.statusCodeHandler.isOverrideListSet()) {
                MatcherAssert.assertThat(String.format("Status code mismatch", requestLog.getRequestLog()), response.getStatusCode(), Matchers.isIn(this.statusCodeHandler.getAndRemoveOverrideList()));
            } else {
                MatcherAssert.assertThat(String.format("Status code mismatch", requestLog.getRequestLog()), response.getStatusCode(), Matchers.isIn(this.statusCodeHandler.get(method)));
            }
        }
    }

    public void addStatusCodeOverride (Integer... codes) {
        this.statusCodeHandler.setOverrideList(Arrays.asList(codes));
    }

    private void logIfError(Response response) {
        ((ValidatableResponse)response.then()).log().ifError();
    }

    private void checkForErrorString (Response response) {
        if(this.errorString != null) {
            MatcherAssert.assertThat("Error String found in response: "+ response.getBody().asString(), response.getBody().asString().contains(this.errorString), Is.is(false));
        }
    }

    private boolean isSessionInvalid (Response response, int retries) {

    }
}
