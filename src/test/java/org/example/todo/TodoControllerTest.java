package org.example.todo;


import org.example.todo.entity.Todo;
import org.example.todo.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    void cleanTodos() {
        todoRepository.deleteAll();
    }

    @Test
    void should_response_empty_list_when_index_with_no_any_todo() throws Exception {
        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void should_response_empty_list_when_index_with_one_todo() throws Exception {
        Todo todo = new Todo(null, "Buy milk", false);
        todoRepository.save(todo);

        MockHttpServletRequestBuilder request = get("/todos")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].text").value("Buy milk"))
                .andExpect(jsonPath("$[0].done").value(false));
    }


    /*
         Scenario: Create a new todo successfully
        Given the storage is available
        When a client POSTs to /todos with body:
     */

    @Test
    void should_return_successfully_when_create_a_new_todo() throws Exception {
        String requestBody = """
                {
                    "text": "Buy milk",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].text").value("Buy milk"))
                .andExpect(jsonPath("$[0].done").value(false));
    }


    /* Scenario: Reject empty text
    When a client POSTs to /todos with body:
      """
      { "text": "", "done": false }
      """
    Then respond 422 Unprocessable Entity
     */

    @Test
    void should_return_422_when_create_a_todo_with_empty_text() throws Exception {
        String requestBody = """
                {
                    "text": "",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isUnprocessableEntity());
    }


    /*Scenario: Ignore client-sent id
    When a client POSTs to /todos with body:
      """
      { "id": "client-sent", "text": "Buy bread", "done": false }
      """
    Then respond 201 Created
      And the server generates and returns its own "id" different from "client-sent"
     */

    @Test
    void should_ignore_client_sent_id_when_create_a_todo() throws Exception {
        String requestBody = """
                {
                    "id": "client-sent",
                    "text": "Buy bread",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(org.hamcrest.Matchers.not("client-sent")))
                .andExpect(jsonPath("$[0].text").value("Buy bread"))
                .andExpect(jsonPath("$[0].done").value(false));
    }


/*Scenario: Update both fields
    Given a todo exists with id "123"
    When a client PUTs to /todos/123 with body:
      """
      { "text": "Buy snacks", "done": true }
      """
    Then respond 200 OK
      And the response body reflects both changes

 */
    @Test
    void should_update_both_fields_when_put_a_todo_with_id_1() throws Exception {
        Todo todo = new Todo(null, "Buy milk", false);
        todoRepository.save(todo);
        String requestBody = """
                {
                    "id": "1",
                    "text": "Buy bread",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/"+ 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Buy bread"))
                .andExpect(jsonPath("$[0].done").value(false));
    }

    /*Scenario: Ignore surplus id in body
    Given a todo exists with id "123"
      And a todo exists with id "456"
    When a client PUTs to /todos/123 with body:
      """
      { "id": "456", ""text": "Buy snacks", "done": true }
      """
    Then respond 200 OK
      And todo with id "123" is updated
      And todo with id "456" is not updated

     */
    @Test
    void should_ignore_surplus_id_in_body_when_put_a_todo_with_id_1() throws Exception {
        Todo todo1 = new Todo(null, "Buy milk", false);
        todoRepository.save(todo1);
        Todo todo2 = new Todo(null, "Buy eggs", false);
        todoRepository.save(todo2);
        String requestBody = """
                {
                    "id": "2",
                    "text": "Buy bread",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/"+ 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Buy bread"))
                .andExpect(jsonPath("$[0].done").value(false));
    }


    /*
        Scenario: Reject update for non-existing id
    Given no todo exists with id "999"
    When a client PUTs to /todos/999 with body:
      """
      { "text": "Buy snacks", "done": true }
      """
    Then respond 404 Not Found
     */

    @Test
    void should_return_404_when_put_a_todo_with_non_existing_id() throws Exception {
        String requestBody = """
                {
                    "text": "Buy bread",
                    "done": false
                }
                """;

        MockHttpServletRequestBuilder request = put("/todos/"+ 999)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    /*
        Scenario: Reject incomplete payload
    When a client PUTs to /todos/123 with an empty JSON object:
      """
      { }
      """
    Then respond 422 Unprocessable Entity
     */
   @Test
   void  should_return_422_when_put_a_todo_with_incomplete_payload() throws Exception {
       Todo todo = new Todo(null, "Buy milk", false);
       todoRepository.save(todo);
       String requestBody = """
                {
                }
                """;

       MockHttpServletRequestBuilder request = put("/todos/"+ 1)
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody);

       mockMvc.perform(request)
               .andExpect(status().isUnprocessableEntity());
   }
}
