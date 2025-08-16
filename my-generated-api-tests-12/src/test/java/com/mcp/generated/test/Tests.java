package com.mcp.generated.test;

import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;
import io.restassured.response.Response;
import com.mcp.generated.steps.Steps;
import com.mcp.generated.steps.dtos.RequestDto;
import com.mcp.generated.steps.dtos.ResponseDto;

public class Tests {

    @Test
    public void should_create_new_post_and_verify_response() {
        // Given
        Steps steps = new Steps("https://jsonplaceholder.typicode.com/posts");

        // Prepare request data
        RequestDto requestBody = new RequestDto();
        requestBody.setTitle("foo");
        requestBody.setBody("bar");
        requestBody.setUserId(1);

        // When
        Response response = steps.postposts(requestBody);

        // Then
        assertThat(response.getStatusCode())
            .as("Status code should start with 2xx")
            .isBetween(200, 299);

        ResponseDto responseDto = response.as(ResponseDto.class);

        // Verify response data
        assertThat(responseDto)
            .as("Response DTO should not be null")
            .isNotNull();

        assertThat(responseDto.getTitle())
            .as("Title should match the request")
            .isEqualTo("foo");

        assertThat(responseDto.getBody())
            .as("Body should match the request")
            .isEqualTo("bar");

        assertThat(responseDto.getUserId())
            .as("UserId should match the request")
            .isEqualTo(1);

        assertThat(responseDto.getId())
            .as("ID should be assigned by the server")
            .isPositive();
    }
}
