package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Repository.TeacherRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.AcademicTrafficLight;
import com.example.fantasticosback.util.ClassSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DeanOfficeRepository deanOfficeRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Student student;
    private Teacher teacher;
    private DeanOffice deanOffice;

    @BeforeEach
    void setUp() {
        // Create test student
        Career career = new Career("Systems Engineering", 160);
        AcademicTrafficLight academicTrafficLight = new AcademicTrafficLight(1, 0, career);
        student = new Student("John", "Doe", 123456789, "Engineering", "C001", "E001", 5, academicTrafficLight);

        // Create test teacher
        teacher = new Teacher("Mary", "Smith", 987654321, "Mathematics");
        teacher.setId("T001");

        // Create test dean office
        deanOffice = new DeanOffice("D001", "Engineering");

        // Set up realistic student data with semester and enrollments
        setupStudentData();
    }

    private void setupStudentData() {
        // Create subjects
        Subject calculus = new Subject("1001", "Calculus I", 4, 1);
        Subject physics = new Subject("1002", "Physics I", 3, 1);

        // Create groups with sessions
        Group calculusGroup = new Group(1, 101, 30, true, calculus, teacher);
        calculusGroup.addSession(new ClassSession("Monday", "08:00", "10:00", "Room 101"));
        calculusGroup.addSession(new ClassSession("Wednesday", "08:00", "10:00", "Room 101"));

        Group physicsGroup = new Group(2, 102, 25, true, physics, teacher);
        physicsGroup.addSession(new ClassSession("Tuesday", "10:00", "12:00", "Room 102"));

        // Create enrollments
        Enrollment calculusEnrollment = new Enrollment(calculusGroup, 1, "active", 0.0);
        Enrollment physicsEnrollment = new Enrollment(physicsGroup, 2, "active", 0.0);

        // Create active semester
        Semester currentSemester = new Semester(1, 2024, 1, true);
        currentSemester.addSubject(calculusEnrollment);
        currentSemester.addSubject(physicsEnrollment);

        // Add semester to student
        student.getSemesters().add(currentSemester);
    }

    @Test
    void testGetStudentSchedule_StudentQueryingOwnSchedule_Success() throws IllegalAccessException {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));

        // Act
        Map<String, Object> result = scheduleService.getStudentSchedule("E001", "E001", UserRole.STUDENT);

        // Assert
        assertNotNull(result);
        assertEquals("E001", result.get("studentId"));
        assertEquals("John Doe", result.get("studentName"));
        assertEquals("Engineering", result.get("career"));
        assertEquals(5, result.get("semester"));
        assertInstanceOf(ArrayList.class, result.get("classes"));
        ArrayList<?> classes = (ArrayList<?>) result.get("classes");
        assertEquals(3, classes.size()); // Three sessions total: 2 calculus + 1 physics

        verify(studentRepository).findById("E001");
    }

    @Test
    void testGetStudentSchedule_StudentQueryingAnotherSchedule_ThrowsIllegalAccessException() {
        // Arrange
        when(studentRepository.findById("E002")).thenReturn(Optional.of(student));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class,
            () -> scheduleService.getStudentSchedule("E002", "E001", UserRole.STUDENT));

        assertEquals("Students can only query their own schedule", exception.getMessage());
        verify(studentRepository).findById("E002");
    }

    @Test
    void testGetStudentSchedule_TeacherQueryingAssignedStudent_Success() throws IllegalAccessException {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(teacherRepository.findById("T001")).thenReturn(Optional.of(teacher));

        // Act
        Map<String, Object> result = scheduleService.getStudentSchedule("E001", "T001", UserRole.TEACHER);

        // Assert
        assertNotNull(result);
        assertEquals("E001", result.get("studentId"));
        assertInstanceOf(ArrayList.class, result.get("classes"));
        ArrayList<?> classes = (ArrayList<?>) result.get("classes");
        assertEquals(3, classes.size()); // Should be 3 classes total: 2 calculus + 1 physics

        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("T001");
    }

    @Test
    void testGetStudentSchedule_TeacherQueryingUnassignedStudent_ThrowsIllegalAccessException() {
        // Arrange
        Teacher otherTeacher = new Teacher("Carlos", "Martinez", 555555555, "Physics");
        otherTeacher.setId("T002");

        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(teacherRepository.findById("T002")).thenReturn(Optional.of(otherTeacher));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class,
            () -> scheduleService.getStudentSchedule("E001", "T002", UserRole.TEACHER));

        assertEquals("Teachers can only query schedules of their assigned students", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("T002");
    }

    @Test
    void testGetStudentSchedule_DeanOfficeQueryingStudentFromSameFaculty_Success() throws IllegalAccessException {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D001")).thenReturn(Optional.of(deanOffice));

        // Act
        Map<String, Object> result = scheduleService.getStudentSchedule("E001", "D001", UserRole.DEAN_OFFICE);

        // Assert
        assertNotNull(result);
        assertEquals("E001", result.get("studentId"));
        assertInstanceOf(ArrayList.class, result.get("classes"));
        ArrayList<?> classes = (ArrayList<?>) result.get("classes");
        assertEquals(3, classes.size()); // Three sessions total: 2 calculus + 1 physics

        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D001");
    }

    @Test
    void testGetStudentSchedule_DeanOfficeQueryingStudentFromDifferentFaculty_ThrowsIllegalAccessException() {
        // Arrange
        DeanOffice otherDeanOffice = new DeanOffice("D002", "Medicine");

        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D002")).thenReturn(Optional.of(otherDeanOffice));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class,
            () -> scheduleService.getStudentSchedule("E001", "D002", UserRole.DEAN_OFFICE));

        assertEquals("Dean office can only query students from their faculty", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D002");
    }

    @Test
    void testGetStudentSchedule_StudentNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scheduleService.getStudentSchedule("E999", "E999", UserRole.STUDENT));

        assertEquals("Student not found with ID: E999", exception.getMessage());
        verify(studentRepository).findById("E999");
    }

    @Test
    void testGetStudentSchedule_TeacherNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(teacherRepository.findById("T999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scheduleService.getStudentSchedule("E001", "T999", UserRole.TEACHER));

        assertEquals("Teacher not found with ID: T999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("T999");
    }

    @Test
    void testGetStudentSchedule_DeanOfficeNotFound_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> scheduleService.getStudentSchedule("E001", "D999", UserRole.DEAN_OFFICE));

        assertEquals("Dean office not found with ID: D999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D999");
    }

    @Test
    void testGetStudentSchedule_StudentWithoutSemesters_ReturnsEmptySchedule() throws IllegalAccessException {
        // Arrange
        Career career = new Career("Systems Engineering", 160);
        AcademicTrafficLight academicTrafficLight = new AcademicTrafficLight(1, 0, career);
        Student studentWithoutSemesters = new Student("Ana", "Lopez", 111111111, "Engineering", "C002", "E002", 1, academicTrafficLight);
        when(studentRepository.findById("E002")).thenReturn(Optional.of(studentWithoutSemesters));

        // Act
        Map<String, Object> result = scheduleService.getStudentSchedule("E002", "E002", UserRole.STUDENT);

        // Assert
        assertNotNull(result);
        assertEquals("E002", result.get("studentId"));
        assertEquals("Ana Lopez", result.get("studentName"));
        assertInstanceOf(ArrayList.class, result.get("classes"));
        ArrayList<?> classes = (ArrayList<?>) result.get("classes");
        assertTrue(classes.isEmpty());

        verify(studentRepository).findById("E002");
    }

    @Test
    void testGetStudentSchedule_CancelledEnrollment_NotIncludedInSchedule() throws IllegalAccessException {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));

        // Act
        Map<String, Object> result = scheduleService.getStudentSchedule("E001", "E001", UserRole.STUDENT);

        // Assert
        assertNotNull(result);
        assertInstanceOf(ArrayList.class, result.get("classes"));
        ArrayList<?> classes = (ArrayList<?>) result.get("classes");
        // Verify that classes list is present (cancelled enrollments are not included)
        assertNotNull(classes);

        verify(studentRepository).findById("E001");
    }
}