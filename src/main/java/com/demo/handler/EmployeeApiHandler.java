package com.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.demo.dao.EmployeeDAO;
import com.demo.model.Employee;
import com.demo.util.JsonUtil;

import java.util.Map;

public class EmployeeApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request,Context context) {
        try {
            LambdaLogger logger = context.getLogger();
            logger.log(request.toString());
            String method = request.getHttpMethod();
            String path = request.getPath();

            switch (method) {
                case "GET":
                    if ("/employees".equals(path)) {
                        return response(200,JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    if (path.startsWith("/employees/")) {

                        int id = Integer.parseInt(request.getPathParameters().get("id"));
                        Employee employee = employeeDAO.getEmployeeById(id);
                        if (employee == null) {
                            return response(404, "Employee Not Found");
                        }
                        return response(200,JsonUtil.toJson(employee));
                    }
                    break;
                case "POST":
                    if ("/employees".equals(path)) {
                        Employee employee = JsonUtil.fromJson(request.getBody(), Employee.class);
                        employeeDAO.addEmployee(employee);
                        return response(201, "Employee Created");
                    }
                    break;
                case "PUT":
                    if (path.startsWith("/employees/")) {
                        int id = Integer.parseInt(request.getPathParameters().get("id"));
                        Employee employee = JsonUtil.fromJson(request.getBody(), Employee.class);
                        boolean isUpdated = employeeDAO.updateEmployee(id, employee);
                        if (isUpdated){
                            return response(200, "Employee Updated");
                        }
                        else{
                            return response(200, "Employee Not Found With this ID");
                        }

                    }
                    break;
                case "DELETE":
                    if (path.startsWith("/employees/")) {
                        int id = Integer.parseInt(request.getPathParameters().get("id"));
                        boolean isDeleted = employeeDAO.deleteEmployee(id);
                        if (isDeleted){
                            return response(200, "Employee Deleted");
                        }
                        else{
                            return response(200, "Employee Not Found With this ID");
                        }
                    }
                    break;
            }

            return response(404, "Route Not Found");

        } catch (Exception e) {

            e.printStackTrace();

            return response(500, e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent response(int status, String body) {

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of(
                        "Content-Type", "application/json",
                        "Access-Control-Allow-Origin", "*"))
                .withBody(body);
    }
}