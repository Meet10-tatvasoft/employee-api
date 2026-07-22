package com.demo.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.demo.dao.EmployeeDAO;
import com.demo.util.JsonUtil;

import java.util.Map;

public class EmployeeApiHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();

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
                    break;

                case "POST":
                    if ("/employees".equals(path)) {
                        return response(200, JsonUtil.toJson(employeeDAO.getAllEmployees()));
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

    private APIGatewayProxyResponseEvent response(int status, String body) {

        return new APIGatewayProxyResponseEvent()
                .withStatusCode(status)
                .withHeaders(Map.of(
                        "Content-Type", "application/json",
                        "Access-Control-Allow-Origin", "*"))
                .withBody(body);
    }
}