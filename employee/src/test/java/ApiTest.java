import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class ApiTest {

    @Test
    public void testEmployeesApi() {
        // Base URL'i ayarlıyoruz
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";

        // GET isteği gönder ve yanıtı al
        Response response = given()
                                .when()
                                .get("/employees")
                                .then()
                                .statusCode(200) 
                                .extract()
                                .response();

       
        response
            .then()
            .assertThat()
            .body("data", hasSize(24)) // 24 adet kaydın geldiğini kontrol etme
            .body("data.find { it.employee_salary == 313500 }.employee_name", equalTo("Haley Kennedy")); 
    }

    @Test
    public void testEmployeeSalariesArePositive() {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";

        given()
            .when()
            .get("/employees")
            .then()
            .statusCode(200)
            .body("data.employee_salary", everyItem(greaterThan(0))); 
    }

    @Test
    public void testSpecificEmployeeDetails() {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";

        given()
            .when()
            .get("/employees")
            .then()
            .statusCode(200)
            .body("data.find { it.employee_name == 'Tiger Nixon' }.employee_salary", equalTo(320800)) 
            .body("data.find { it.employee_name == 'Tiger Nixon' }.employee_age", equalTo(61)); 
    }

    @Test
    public void testEmployeeNamesAreUnique() {
        RestAssured.baseURI = "https://dummy.restapiexample.com/api/v1";

        Response response = given()
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
