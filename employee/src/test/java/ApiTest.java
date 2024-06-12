import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";
    }

    @Test
    @Order(1)
    public void testEmployeesApi() {
        Response response =
            given()
                .when()
                .get("/employees")
                .then()
                .statusCode(200)
                .extract()
                .response();

        response
            .then()
            .assertThat()
            .body("data", hasSize(24))
            .body("data.find { it.employee_salary == 313500 }.employee_name", equalTo("Haley Kennedy"));
    }

    @Test
    @Order(2)
    public void testEmployeeSalariesArePositive() {
        given()
            .when()
            .get("/employees")
            .then()
            .statusCode(200)
            .body("data.employee_salary", everyItem(greaterThan(0)));
    }

    @Test
    @Order(3)
    public void testSpecificEmployeeDetails() {
        given()
            .when()
            .get("/employees")
            .then()
            .statusCode(200)
            .body("data.find { it.employee_name == 'Tiger Nixon' }.employee_salary", equalTo(320800))
            .body("data.find { it.employee_name == 'Tiger Nixon' }.employee_age", equalTo(61));
    }

    @Test
    @Order(4)
    public void testEmployeeNamesAreUnique() {
        Response response =
            given()
                .when()
                .get("/employees")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<String> employeeNames = response.jsonPath().getList("data.employee_name");
        Set<String> uniqueEmployeeNames = new HashSet<>(employeeNames);

        assert (employeeNames.size() == uniqueEmployeeNames.size());
    }
}
