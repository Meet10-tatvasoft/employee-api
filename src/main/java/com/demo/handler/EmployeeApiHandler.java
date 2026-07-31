package com.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.demo.dao.EmployeeDAO;
import com.demo.model.Employee;
import com.demo.util.JsonUtil;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import software.amazon.awssdk.services.eventbridge.model.PutEventsResponse;

import java.util.List;
import java.util.Map;

public class EmployeeApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final EventBridgeClient eventBridgeClient = EventBridgeClient.builder().build();
    private static final String EVENT_BUS_NAME = System.getenv("EVENT_BUS_NAME");

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        try {

            LambdaLogger logger = context.getLogger();
            logger.log(request.toString());

            String method = request.getHttpMethod();
            String path = request.getPath();

            switch (method) {

                case "GET":
                    if ("/employees".equals(path) || path.startsWith("/employees/")) {
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    if("/demo-api".equals(path)){
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    if("/demo-api-2".equals(path)){
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    if("/demo-api-3".equals(path)){
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }if("/demo-api-4".equals(path)){
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    if("/demo-api-5".equals(path)){
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    break;

                case "POST":
                    if ("/employees".equals(path)) {
                        List<Employee> employees = employeeDAO.getAllEmployees();
                        publishEmployeeCreatedEvent(employees.get(0), context.getLogger());
                        return response(200, JsonUtil.toJson(employees));
                    }
                    break;

                case "PUT":
                    if (path.startsWith("/employees/")) {
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    break;

                case "DELETE":
                    if (path.startsWith("/employees/")) {
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
                    }
                    break;
            }

            return response(404, "Route Not Found");

        } catch (Exception e) {

            e.printStackTrace();

            return response(500, e.getMessage());
        }
    }
    private void publishEmployeeCreatedEvent(Employee employee, LambdaLogger logger) {
        try {
            PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                    .eventBusName(EVENT_BUS_NAME)
                    .source("com.demo.employeeapi")
                    .detailType("EmployeeCreated")
                    .detail(JsonUtil.toJson(employee))
                    .build();

            PutEventsResponse result = eventBridgeClient.putEvents(
                    PutEventsRequest.builder().entries(entry).build());

            if (result.failedEntryCount() > 0) {
                logger.log("EventBridge PutEvents failed: "
                        + result.entries().get(0).errorMessage());
            }
        } catch (Exception e) {
            // Deliberately swallowed, not rethrown - a failure to publish the
            // event should never fail the actual API response. This is the
            // whole point of decoupling side effects via EventBridge.
            logger.log("Error publishing EmployeeCreated event: " + e.getMessage());
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