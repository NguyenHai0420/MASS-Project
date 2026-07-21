package com.group_project.MASS.config;

import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private ClinicInformationRepository clinicInformationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Specialties if empty
        if (specialtyRepository.count() == 0) {
            Specialty spec1 = Specialty.builder().name("Tim mạch").description("Khoa Tim mạch").imageUrl("https://images.unsplash.com/photo-1579684389782-64d84b5e905d").build();
            Specialty spec2 = Specialty.builder().name("Nhi khoa").description("Khoa Nhi").imageUrl("https://images.unsplash.com/photo-1581594693702-fbdc51b2763b").build();
            Specialty spec3 = Specialty.builder().name("Nội khoa").description("Khoa Nội").build();
            Specialty spec4 = Specialty.builder().name("Ngoại khoa").description("Khoa Ngoại").build();
            Specialty spec5 = Specialty.builder().name("Da liễu").description("Khoa Da liễu").imageUrl("https://images.unsplash.com/photo-1612349317150-e413f6a5b16d").build();
            Specialty spec6 = Specialty.builder().name("Mắt").description("Khoa Mắt").build();
            Specialty spec7 = Specialty.builder().name("Tai Mũi Họng").description("Khoa Tai Mũi Họng").build();
            Specialty spec8 = Specialty.builder().name("Răng Hàm Mặt").description("Khoa Răng Hàm Mặt").build();
            specialtyRepository.saveAll(List.of(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8));
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

        // 3. Seed Doctors if empty
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

            Specialty specialty = specialtyRepository.findAll().get(0); // Tim mạch
            DoctorProfile doctorProfile = DoctorProfile.builder()
                    .user(doctorUser)
                    .specialty(specialty)
                    .degree("MD, PhD")
                    .experience("10 years of experience")
                    .description("Specialist in heart transplantation and cardiovascular diseases.")
                    .build();
            doctorProfileRepository.save(doctorProfile);
        }

        if (userRepository.findByEmail("doctor1@mass.com").isEmpty()) {
            Specialty spec1 = specialtyRepository.findAll().get(0);
            Specialty spec2 = specialtyRepository.findAll().size() > 1 ? specialtyRepository.findAll().get(1) : spec1;

            User doc1 = User.builder().email("doctor1@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Nguyen Van Anh").role(Role.ROLE_DOCTOR).build();
            User doc2 = User.builder().email("doctor2@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Tran Thi Binh").role(Role.ROLE_DOCTOR).build();
            User doc3 = User.builder().email("doctor3@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Le Van Huy").role(Role.ROLE_DOCTOR).build();
            User doc4 = User.builder().email("doctor4@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Pham Thi Duyen").role(Role.ROLE_DOCTOR).build();
            User doc5 = User.builder().email("doctor5@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Hoang Van Hoa").role(Role.ROLE_DOCTOR).build();
            User doc6 = User.builder().email("doctor6@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Vu Thi Nga").role(Role.ROLE_DOCTOR).build();
            User doc7 = User.builder().email("doctor7@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Ngo Van Giang").role(Role.ROLE_DOCTOR).build();

            userRepository.saveAll(List.of(doc1, doc2, doc3, doc4, doc5, doc6, doc7));

            List<Specialty> specs = specialtyRepository.findAll();
            DoctorProfile prof1 = DoctorProfile.builder().user(doc1).specialty(specs.get(0)).degree("Tiến sĩ").experience("10 năm").build();
            DoctorProfile prof2 = DoctorProfile.builder().user(doc2).specialty(specs.size() > 1 ? specs.get(1) : specs.get(0)).degree("Thạc sĩ").experience("5 năm").build();
            DoctorProfile prof3 = DoctorProfile.builder().user(doc3).specialty(specs.size() > 2 ? specs.get(2) : specs.get(0)).degree("Bác sĩ chuyên khoa I").experience("8 năm").build();
            DoctorProfile prof4 = DoctorProfile.builder().user(doc4).specialty(specs.size() > 3 ? specs.get(3) : specs.get(0)).degree("Tiến sĩ").experience("12 năm").build();
            DoctorProfile prof5 = DoctorProfile.builder().user(doc5).specialty(specs.size() > 4 ? specs.get(4) : specs.get(0)).degree("Thạc sĩ").experience("6 năm").build();
            DoctorProfile prof6 = DoctorProfile.builder().user(doc6).specialty(specs.size() > 5 ? specs.get(5) : specs.get(0)).degree("Bác sĩ chuyên khoa II").experience("15 năm").build();
            DoctorProfile prof7 = DoctorProfile.builder().user(doc7).specialty(specs.size() > 6 ? specs.get(6) : specs.get(0)).degree("Thạc sĩ").experience("4 năm").build();

            doctorProfileRepository.saveAll(List.of(prof1, prof2, prof3, prof4, prof5, prof6, prof7));

            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(5);

            List<DoctorProfile> newProfiles = List.of(prof1, prof2, prof3, prof4, prof5, prof6, prof7);
            List<Schedule> newSchedules = new ArrayList<>();

            for (DoctorProfile prof : newProfiles) {
                LocalDate currentDate = today;
                while (!currentDate.isAfter(endDate)) {
                    newSchedules.add(Schedule.builder().doctorProfile(prof).date(currentDate).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(8, 30)).isAvailable(true).build());
                    newSchedules.add(Schedule.builder().doctorProfile(prof).date(currentDate).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).isAvailable(true).build());
                    newSchedules.add(Schedule.builder().doctorProfile(prof).date(currentDate).startTime(LocalTime.of(10, 0)).endTime(LocalTime.of(10, 30)).isAvailable(true).build());
                    newSchedules.add(Schedule.builder().doctorProfile(prof).date(currentDate).startTime(LocalTime.of(13, 30)).endTime(LocalTime.of(14, 0)).isAvailable(true).build());
                    currentDate = currentDate.plusDays(1);
                }
            }
            scheduleRepository.saveAll(newSchedules);
        }

        // 4. Seed Patients if empty
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

        if (userRepository.findByEmail("patient@test.com").isEmpty()) {
            User patient = User.builder()
                    .email("patient@test.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Nguyễn Bệnh Nhân")
                    .role(Role.ROLE_PATIENT)
                    .build();
            userRepository.save(patient);
        }

        if (clinicInformationRepository.count() == 0) {
            ClinicInformation clinic = ClinicInformation.builder()
                    .name("Phòng khám Đa khoa MASS")
                    .address("Số 1 Đại Cồ Việt, Bách Khoa, Hai Bà Trưng, Hà Nội")
                    .phone("024.1234.5678")
                    .email("contact@massclinic.vn")
                    .workingHours("Thứ 2 - Thứ 7: 8:00 - 17:30")
                    .build();
            clinicInformationRepository.save(clinic);
        }
    }
}
