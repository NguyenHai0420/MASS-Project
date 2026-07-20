package com.group_project.MASS.config;

import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Specialties if empty
        if (specialtyRepository.count() == 0) {
            Specialty cardio = Specialty.builder()
                    .name("Cardiology")
                    .description("Heart care and surgery")
                    .imageUrl("https://images.unsplash.com/photo-1579684389782-64d84b5e905d")
                    .build();
            Specialty pedia = Specialty.builder()
                    .name("Pediatrics")
                    .description("Child care and healthcare")
                    .imageUrl("https://images.unsplash.com/photo-1581594693702-fbdc51b2763b")
                    .build();
            Specialty derma = Specialty.builder()
                    .name("Dermatology")
                    .description("Skin care and treatment")
                    .imageUrl("https://images.unsplash.com/photo-1612349317150-e413f6a5b16d")
                    .build();

            specialtyRepository.save(cardio);
            specialtyRepository.save(pedia);
            specialtyRepository.save(derma);
        }

        // 2. Seed Admin if empty
        if (userRepository.findByEmail("admin@mass.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@mass.com")
                    .password(passwordEncoder.encode("password123"))
                    .fullName("System Administrator")
                    .phone("0987654321")
                    .gender("Male")
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }

        // 3. Seed Doctor if empty
        if (userRepository.findByEmail("doctor@mass.com").isEmpty()) {
            User doctorUser = User.builder()
                    .email("doctor@mass.com")
                    .password(passwordEncoder.encode("doctor@123"))
                    .fullName("Dr. John Doe")
                    .phone("0123456789")
                    .gender("Male")
                    .role(Role.ROLE_DOCTOR)
                    .build();
            doctorUser = userRepository.save(doctorUser);

            Specialty specialty = specialtyRepository.findAll().get(0); // Cardiology
            DoctorProfile doctorProfile = DoctorProfile.builder()
                    .user(doctorUser)
                    .specialty(specialty)
                    .degree("MD, PhD")
                    .experience("10 years of experience")
                    .description("Specialist in heart transplantation and cardiovascular diseases.")
                    .build();
            doctorProfile = doctorProfileRepository.save(doctorProfile);

            // Seed Schedule for Doctor
            if (scheduleRepository.count() == 0) {
                Schedule schedule1 = Schedule.builder()
                        .doctorProfile(doctorProfile)
                        .date(LocalDate.now().plusDays(1))
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(12, 0))
                        .isAvailable(true)
                        .build();

                Schedule schedule2 = Schedule.builder()
                        .doctorProfile(doctorProfile)
                        .date(LocalDate.now().plusDays(2))
                        .startTime(LocalTime.of(13, 0))
                        .endTime(LocalTime.of(17, 0))
                        .isAvailable(true)
                        .build();

                scheduleRepository.save(schedule1);
                scheduleRepository.save(schedule2);
            }
        }

        // 4. Seed Patients/Users if empty
        if (userRepository.findByEmail("patient@mass.com").isEmpty()) {
            User patient = User.builder()
                    .email("patient@mass.com")
                    .password(passwordEncoder.encode("password123"))
                    .fullName("Alice Patient")
                    .phone("0333333333")
                    .gender("Female")
                    .role(Role.ROLE_PATIENT)
                    .build();
            userRepository.save(patient);
        }

        // Seed an Appointment if possible
        if (appointmentRepository.count() == 0) {
            User patient = userRepository.findByEmail("patient@mass.com").orElse(null);
            if (patient != null && !doctorProfileRepository.findAll().isEmpty() && !scheduleRepository.findAll().isEmpty()) {
                DoctorProfile dp = doctorProfileRepository.findAll().get(0);
                Schedule sc = scheduleRepository.findAll().get(0);

                Appointment appointment = Appointment.builder()
                        .patient(patient)
                        .doctorProfile(dp)
                        .schedule(sc)
                        .status(AppointmentStatus.PENDING)
                        .reason("Regular cardiovascular checkup")
                        .queueNumber(1)
                        .type(AppointmentType.ONLINE)
                        .build();

                appointmentRepository.save(appointment);
            }
        }
    }
}
