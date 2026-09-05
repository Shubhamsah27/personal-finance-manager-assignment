package com.example.finance;

import com.example.finance.category.CategoryRepository;
import com.example.finance.goal.GoalRepository;
import com.example.finance.transaction.TransactionRepository;
import com.example.finance.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
class ApiIntegrationTests {
 @Autowired MockMvc mvc;@Autowired ObjectMapper json;@Autowired UserRepository users;@Autowired TransactionRepository transactions;@Autowired GoalRepository goals;@Autowired CategoryRepository categories;
 @BeforeEach void clean(){transactions.deleteAll();goals.deleteAll();categories.findAll().stream().filter(c->!c.isDefault()).forEach(categories::delete);users.deleteAll();}
 @Test void authenticationAndFinanceFlow()throws Exception{
  register("alex@example.com");
  mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(user("alex@example.com"))).andExpect(status().isConflict());
  mvc.perform(get("/api/transactions")).andExpect(status().isUnauthorized());
  HttpSession session=login("alex@example.com");
  mvc.perform(get("/api/categories").session((org.springframework.mock.web.MockHttpSession)session)).andExpect(status().isOk()).andExpect(jsonPath("$.categories.length()").value(7));
  mvc.perform(post("/api/transactions").session((org.springframework.mock.web.MockHttpSession)session).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":50000,\"date\":\"2025-01-15\",\"category\":\"Salary\",\"description\":\"Salary\"}" )).andExpect(status().isCreated()).andExpect(jsonPath("$.type").value("INCOME"));
  mvc.perform(post("/api/transactions").session((org.springframework.mock.web.MockHttpSession)session).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":12000,\"date\":\"2025-01-16\",\"category\":\"Rent\"}" )).andExpect(status().isCreated());
  mvc.perform(get("/api/reports/monthly/2025/1").session((org.springframework.mock.web.MockHttpSession)session)).andExpect(status().isOk()).andExpect(jsonPath("$.totalIncome.Salary").value(50000)).andExpect(jsonPath("$.netSavings").value(38000));
  mvc.perform(post("/api/goals").session((org.springframework.mock.web.MockHttpSession)session).contentType(MediaType.APPLICATION_JSON).content("{\"goalName\":\"Emergency fund\",\"targetAmount\":100000,\"targetDate\":\"2027-01-01\",\"startDate\":\"2025-01-01\"}" )).andExpect(status().isCreated()).andExpect(jsonPath("$.currentProgress").value(38000));
  mvc.perform(post("/api/auth/logout").session((org.springframework.mock.web.MockHttpSession)session)).andExpect(status().isOk());
  mvc.perform(get("/api/goals").session((org.springframework.mock.web.MockHttpSession)session)).andExpect(status().isUnauthorized());
 }
 @Test void validatesFutureDateAndProtectsDefaults()throws Exception{register("sam@example.com");HttpSession s=login("sam@example.com");mvc.perform(post("/api/transactions").session((org.springframework.mock.web.MockHttpSession)s).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":10,\"date\":\"2099-01-01\",\"category\":\"Food\"}" )).andExpect(status().isBadRequest());mvc.perform(delete("/api/categories/Food").session((org.springframework.mock.web.MockHttpSession)s)).andExpect(status().isForbidden());}
 @Test void customCategoryLifecycleAndTransactionMutation()throws Exception{
  register("casey@example.com");var s=(org.springframework.mock.web.MockHttpSession)login("casey@example.com");
  mvc.perform(post("/api/categories").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Freelance\",\"type\":\"INCOME\"}")).andExpect(status().isCreated()).andExpect(jsonPath("$.isCustom").value(true));
  mvc.perform(post("/api/categories").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"freelance\",\"type\":\"INCOME\"}")).andExpect(status().isConflict());
  MvcResult made=mvc.perform(post("/api/transactions").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1200,\"date\":\"2025-03-10\",\"category\":\"Freelance\"}")).andExpect(status().isCreated()).andReturn();long id=json.readTree(made.getResponse().getContentAsString()).get("id").asLong();
  mvc.perform(delete("/api/categories/Freelance").session(s)).andExpect(status().isBadRequest());
  mvc.perform(put("/api/transactions/"+id).session(s).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":1400,\"description\":\"Revised\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.amount").value(1400));
  mvc.perform(put("/api/transactions/"+id).session(s).contentType(MediaType.APPLICATION_JSON).content("{\"date\":\"2025-03-11\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.date").value("2025-03-10"));
  mvc.perform(delete("/api/transactions/"+id).session(s)).andExpect(status().isOk());
  mvc.perform(get("/api/reports/yearly/2025").session(s)).andExpect(status().isOk()).andExpect(jsonPath("$.netSavings").value(0));
  mvc.perform(delete("/api/categories/Freelance").session(s)).andExpect(status().isBadRequest());
  mvc.perform(post("/api/categories").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Temporary\",\"type\":\"EXPENSE\"}")).andExpect(status().isCreated());
  mvc.perform(delete("/api/categories/Temporary").session(s)).andExpect(status().isOk());
 }
 @Test void isolatesTransactionsAndGoalsAcrossUsers()throws Exception{
  register("one@example.com");register("two@example.com");var one=(org.springframework.mock.web.MockHttpSession)login("one@example.com");var two=(org.springframework.mock.web.MockHttpSession)login("two@example.com");
  MvcResult tx=mvc.perform(post("/api/transactions").session(one).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":99,\"date\":\"2025-02-01\",\"category\":\"Food\"}")).andExpect(status().isCreated()).andReturn();long txId=json.readTree(tx.getResponse().getContentAsString()).get("id").asLong();
  mvc.perform(delete("/api/transactions/"+txId).session(two)).andExpect(status().isForbidden());
  mvc.perform(get("/api/transactions").session(two)).andExpect(status().isOk()).andExpect(jsonPath("$.transactions.length()").value(0));
  MvcResult goal=mvc.perform(post("/api/goals").session(one).contentType(MediaType.APPLICATION_JSON).content("{\"goalName\":\"Laptop\",\"targetAmount\":50000,\"targetDate\":\"2027-06-01\"}")).andExpect(status().isCreated()).andReturn();long goalId=json.readTree(goal.getResponse().getContentAsString()).get("id").asLong();
  mvc.perform(get("/api/goals/"+goalId).session(two)).andExpect(status().isForbidden());
  mvc.perform(put("/api/goals/"+goalId).session(one).contentType(MediaType.APPLICATION_JSON).content("{\"targetAmount\":60000,\"targetDate\":\"2027-08-01\"}")).andExpect(status().isOk()).andExpect(jsonPath("$.targetAmount").value(60000));
  mvc.perform(delete("/api/goals/"+goalId).session(one)).andExpect(status().isOk());
 }
 @Test void filtersTransactionsAndHandlesMissingResources()throws Exception{
  register("filter@example.com");var s=(org.springframework.mock.web.MockHttpSession)login("filter@example.com");
  mvc.perform(post("/api/transactions").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":100,\"date\":\"2025-04-01\",\"category\":\"Food\"}")).andExpect(status().isCreated());
  mvc.perform(post("/api/transactions").session(s).contentType(MediaType.APPLICATION_JSON).content("{\"amount\":200,\"date\":\"2025-05-01\",\"category\":\"Salary\"}")).andExpect(status().isCreated());
  mvc.perform(get("/api/transactions?startDate=2025-04-15&type=INCOME").session(s)).andExpect(status().isOk()).andExpect(jsonPath("$.transactions.length()").value(1));
  mvc.perform(get("/api/transactions?startDate=2025-06-01&endDate=2025-01-01").session(s)).andExpect(status().isBadRequest());
  mvc.perform(delete("/api/transactions/999999").session(s)).andExpect(status().isNotFound());
  mvc.perform(get("/api/goals/999999").session(s)).andExpect(status().isNotFound());
  mvc.perform(get("/api/reports/monthly/2025/13").session(s)).andExpect(status().isBadRequest());
 }
 private void register(String email)throws Exception{mvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(user(email))).andExpect(status().isCreated());}
 private HttpSession login(String email)throws Exception{MvcResult r=mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\""+email+"\",\"password\":\"password123\"}" )).andExpect(status().isOk()).andReturn();return r.getRequest().getSession(false);}
 private String user(String email){return "{\"username\":\""+email+"\",\"password\":\"password123\",\"fullName\":\"Alex Doe\",\"phoneNumber\":\"+919876543210\"}";}
}
