package com.group_project.MASS.config;

import com.group_project.MASS.model.*;
import com.group_project.MASS.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (specialtyRepository.count() == 0) {
            // Seed Specialties
            Specialty spec1 = Specialty.builder().name("Tim mạch").description("Khoa Tim mạch").build();
            Specialty spec2 = Specialty.builder().name("Nhi khoa").description("Khoa Nhi").build();
            Specialty spec3 = Specialty.builder().name("Nội khoa").description("Khoa Nội").build();
            Specialty spec4 = Specialty.builder().name("Ngoại khoa").description("Khoa Ngoại").build();
            Specialty spec5 = Specialty.builder().name("Da liễu").description("Khoa Da liễu").build();
            Specialty spec6 = Specialty.builder().name("Mắt").description("Khoa Mắt").build();
            Specialty spec7 = Specialty.builder().name("Tai Mũi Họng").description("Khoa Tai Mũi Họng").build();
            Specialty spec8 = Specialty.builder().name("Răng Hàm Mặt").description("Khoa Răng Hàm Mặt").build();
            specialtyRepository.saveAll(List.of(spec1, spec2, spec3, spec4, spec5, spec6, spec7, spec8));

           
            User doc1 = User.builder().email("doctor1@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Nguyen Van Anh").role(Role.ROLE_DOCTOR).build();
            User doc2 = User.builder().email("doctor2@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Tran Thi Binh").role(Role.ROLE_DOCTOR).build();
            User doc3 = User.builder().email("doctor3@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Le Van Huy").role(Role.ROLE_DOCTOR).build();
            User doc4 = User.builder().email("doctor4@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Pham Thi Duyen").role(Role.ROLE_DOCTOR).build();
            User doc5 = User.builder().email("doctor5@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Hoang Van Hoa").role(Role.ROLE_DOCTOR).build();
            User doc6 = User.builder().email("doctor6@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Vu Thi Nga").role(Role.ROLE_DOCTOR).build();
            User doc7 = User.builder().email("doctor7@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Ngo Van Giang").role(Role.ROLE_DOCTOR).build();
            
            
            User patient = User.builder()
                    .email("patient@test.com")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("Nguyễn Bệnh Nhân")
                    .role(Role.ROLE_PATIENT)
                    .build();

            userRepository.saveAll(List.of(doc1, doc2, doc3, doc4, doc5, doc6, doc7, patient));

            
            DoctorProfile prof1 = DoctorProfile.builder().user(doc1).specialty(spec1).degree("Tiến sĩ").experience("10 năm").build();
            DoctorProfile prof2 = DoctorProfile.builder().user(doc2).specialty(spec2).degree("Thạc sĩ").experience("5 năm").build();
            DoctorProfile prof3 = DoctorProfile.builder().user(doc3).specialty(spec3).degree("Bác sĩ chuyên khoa I").experience("8 năm").build();
            DoctorProfile prof4 = DoctorProfile.builder().user(doc4).specialty(spec4).degree("Tiến sĩ").experience("12 năm").build();
            DoctorProfile prof5 = DoctorProfile.builder().user(doc5).specialty(spec5).degree("Thạc sĩ").experience("6 năm").build();
            DoctorProfile prof6 = DoctorProfile.builder().user(doc6).specialty(spec6).degree("Bác sĩ chuyên khoa II").experience("15 năm").build();
            DoctorProfile prof7 = DoctorProfile.builder().user(doc7).specialty(spec7).degree("Thạc sĩ").experience("4 năm").build();

            doctorProfileRepository.saveAll(List.of(prof1, prof2, prof3, prof4, prof5, prof6, prof7));


            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            
            Schedule s1 = Schedule.builder().doctorProfile(prof1).date(today).startTime(LocalTime.of(8, 0)).endTime(LocalTime.of(8, 30)).isAvailable(true).build();
            Schedule s2 = Schedule.builder().doctorProfile(prof1).date(today).startTime(LocalTime.of(9, 0)).endTime(LocalTime.of(9, 30)).isAvailable(true).build();
            Schedule s3 = Schedule.builder().doctorProfile(prof2).date(tomorrow).startTime(LocalTime.of(14, 0)).endTime(LocalTime.of(14, 30)).isAvailable(true).build();

            scheduleRepository.saveAll(List.of(s1, s2, s3));

     
            LocalDate endDate = today.withDayOfMonth(24);
            if (endDate.isBefore(today)) {
                endDate = today.plusDays(5);
            }

            List<DoctorProfile> newProfiles = List.of(prof3, prof4, prof5, prof6, prof7);
            java.util.ArrayList<Schedule> newSchedules = new java.util.ArrayList<>();
            
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

            System.out.println("✅ Data seeding completed!");
        } else if (doctorProfileRepository.count() <= 2) {
            System.out.println("⚠️ Found existing DB, but only 2 doctors. Seeding the 5 new doctors...");


            List<Specialty> allSpecialties = specialtyRepository.findAll();
            Specialty spec3 = allSpecialties.stream().filter(s -> s.getName().equals("Nội khoa")).findFirst().orElse(allSpecialties.get(0));
            Specialty spec4 = allSpecialties.stream().filter(s -> s.getName().equals("Ngoại khoa")).findFirst().orElse(allSpecialties.get(0));
            Specialty spec5 = allSpecialties.stream().filter(s -> s.getName().equals("Da liễu")).findFirst().orElse(allSpecialties.get(0));
            Specialty spec6 = allSpecialties.stream().filter(s -> s.getName().equals("Mắt")).findFirst().orElse(allSpecialties.get(0));
            Specialty spec7 = allSpecialties.stream().filter(s -> s.getName().equals("Tai Mũi Họng")).findFirst().orElse(allSpecialties.get(0));

            User doc3 = User.builder().email("doctor3@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Le Van Huy").role(Role.ROLE_DOCTOR).build();
            User doc4 = User.builder().email("doctor4@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Pham Thi Duyen").role(Role.ROLE_DOCTOR).build();
            User doc5 = User.builder().email("doctor5@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Hoang Van Hoa").role(Role.ROLE_DOCTOR).build();
            User doc6 = User.builder().email("doctor6@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Vu Thi Nga").role(Role.ROLE_DOCTOR).build();
            User doc7 = User.builder().email("doctor7@mass.com").password(passwordEncoder.encode("123456")).fullName("Dr. Ngo Van Giang").role(Role.ROLE_DOCTOR).build();
            userRepository.saveAll(List.of(doc3, doc4, doc5, doc6, doc7));

            DoctorProfile prof3 = DoctorProfile.builder().user(doc3).specialty(spec3).degree("Bác sĩ chuyên khoa I").experience("8 năm").build();
            DoctorProfile prof4 = DoctorProfile.builder().user(doc4).specialty(spec4).degree("Tiến sĩ").experience("12 năm").build();
            DoctorProfile prof5 = DoctorProfile.builder().user(doc5).specialty(spec5).degree("Thạc sĩ").experience("6 năm").build();
            DoctorProfile prof6 = DoctorProfile.builder().user(doc6).specialty(spec6).degree("Bác sĩ chuyên khoa II").experience("15 năm").build();
            DoctorProfile prof7 = DoctorProfile.builder().user(doc7).specialty(spec7).degree("Thạc sĩ").experience("4 năm").build();
            doctorProfileRepository.saveAll(List.of(prof3, prof4, prof5, prof6, prof7));

            LocalDate today = LocalDate.now();
            LocalDate endDate = today.withDayOfMonth(24);
            if (endDate.isBefore(today)) {
                endDate = today.plusDays(5);
            }

            List<DoctorProfile> newProfiles = List.of(prof3, prof4, prof5, prof6, prof7);
            java.util.ArrayList<Schedule> newSchedules = new java.util.ArrayList<>();
            
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
            System.out.println("✅ Data seeding for 5 new doctors completed!");
        }
    }
}
