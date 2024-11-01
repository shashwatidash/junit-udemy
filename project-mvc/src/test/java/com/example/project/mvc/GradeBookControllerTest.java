package com.example.project.mvc;

import com.example.project.mvc.models.CollegeStudent;
import com.example.project.mvc.models.GradeBookCollegeStudent;
import com.example.project.mvc.models.MathGrade;
import com.example.project.mvc.repository.MathGradesDao;
import com.example.project.mvc.repository.StudentDao;
import com.example.project.mvc.service.StudentAndGradeService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class GradeBookControllerTest {

   private static MockHttpServletRequest request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbc;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private StudentAndGradeService studentAndGradeService;

    @Autowired
    private StudentDao studentDao;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Autowired
    private MathGradesDao mathGradesDao;

    @BeforeAll
    public static void setUp() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Chad");
        request.setParameter("lastname", "Darby");
        request.setParameter("emailAddress", "chad.darby@udemy.com");
    }

    @BeforeEach
    public void beforeEach() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
    }

    @Test
    public void getStudentsHttpRequest() throws Exception {
        CollegeStudent studentOne = new GradeBookCollegeStudent("Eric", "Roby",
                "eric.roby@udemy.com");
        CollegeStudent studentTwo = new GradeBookCollegeStudent("Chad", "Darby",
                "chad.darby@udemy.com");

        List<CollegeStudent> students = new ArrayList<>(Arrays.asList(studentOne, studentTwo));
        when(studentCreateServiceMock.getGradeBook()).thenReturn(students);
        assertIterableEquals(students, studentCreateServiceMock.getGradeBook());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "index");
    }

    @Test
    public void createStudentHttpRequest() throws Exception {

        CollegeStudent studentOne = new CollegeStudent("Eric", "Roby", "eric.roby@outlook.com");
        List<CollegeStudent> collegeStudentList = new ArrayList<>(List.of(studentOne));
        when(studentCreateServiceMock.getGradeBook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradeBook());

        MvcResult mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", Objects.requireNonNull(request.getParameterValues("firstname")))
                .param("lastname", Objects.requireNonNull(request.getParameterValues("lastname")))
                .param("emailAddress", Objects.requireNonNull(request.getParameterValues("emailAddress"))))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.darby@udemy.com");
        assertNotNull(verifyStudent);
    }

    @Test
    public void deleteStudentHttpRequest() throws Exception {
        assertTrue(studentDao.findById(2).isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 2))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "index");

        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    public void deleteStudentHttpRequestErrorPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/delete/student/{id}", 0))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void studentInformationHttpRequest() throws Exception {
        assertTrue(studentDao.findById(2).isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/studentInformation/{id}", 2))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "studentInformation");
    }

    @Test
    public void studentInformationDoesNotExistsRequest() throws Exception {
        assertFalse(studentDao.findById(0).isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/studentInformation/{id}", 0))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void  createValidGradeHttpRequest() throws Exception {
        assertTrue(studentDao.findById(2).isPresent());
        GradeBookCollegeStudent student = studentAndGradeService.studentInformation(2);
        assertEquals(1, student.getStudentGrades().getMathGradesResult().size());

        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "math")
                        .param("studentId", "2"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "studentInformation");
        student = studentAndGradeService.studentInformation(2);
        assertEquals(2, student.getStudentGrades().getMathGradesResult().size());
    }

    @Test
    public void createGradeHttpRequestInvalidStudent() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                            .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "math")
                        .param("studentId", "0"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void createGradeHttpRequestInvalidGradeType() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(post("/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("grade", "85.0")
                        .param("gradeType", "literature")
                        .param("studentId", "2"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @Test
    public void deleteValidGradeHttpRequest() throws Exception {
        Optional<MathGrade> mathGrade = mathGradesDao.findById(2);
        assertTrue(mathGrade.isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/grades/{id}/{gradeType}", 2, "math"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "studentInformation");
        mathGrade = mathGradesDao.findById(2);
        assertFalse(mathGrade.isPresent());
    }

    @Test
    public void deleteValidGradeHttpRequestInvalidStudent() throws Exception {
        Optional<MathGrade> mathGrade = mathGradesDao.findById(0);
        assertFalse(mathGrade.isPresent());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/grades/{id}/{gradeType}", 1, "math"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
        mathGrade = mathGradesDao.findById(1);
        assertFalse(mathGrade.isPresent());
    }

    @Test
    public void deleteValidGradeHttpRequestInvalidGradeType() throws Exception {
        Optional<MathGrade> mathGrade = mathGradesDao.findById(2);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/grades/{id}/{gradeType}", 2, "literature"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        assertNotNull(mav);
        ModelAndViewAssert.assertViewName(mav, "error");
    }

    @AfterEach
    public void setUpAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
    }
}
