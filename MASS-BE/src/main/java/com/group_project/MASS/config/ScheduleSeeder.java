package com.group_project.MASS.config;

import com.group_project.MASS.model.DoctorProfile;
import com.group_project.MASS.model.Schedule;
import com.group_project.MASS.repository.DoctorProfileRepository;
import com.group_project.MASS.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleSeeder implements CommandLineRunner {

    private final DoctorProfileRepository doctorProfileRepository;
    private final ScheduleRepository scheduleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Chỉ chạy nếu chưa có lịch trình nào trong DB (hoặc có thể kiểm tra từng bác sĩ)
        List<DoctorProfile> allDoctors = doctorProfileRepository.findAll();
        
        if (allDoctors.isEmpty()) {
            return; // Không có bác sĩ nào
        }

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(30); // Tạo lịch cho 30 ngày tới

        List<Schedule> newSchedules = new ArrayList<>();
        
        for (DoctorProfile doctor : allDoctors) {
            // Kiểm tra xem bác sĩ này đã có lịch ngày mai chưa, nếu chưa thì tạo
            boolean hasSchedule = !scheduleRepository.findByDoctorProfileIdAndDate(doctor.getId(), today.plusDays(1)).isEmpty();
            
            if (!hasSchedule) {
                LocalDate currentDate = today;
                while (!currentDate.isAfter(endDate)) {
                    // Các khung giờ làm việc: 08:00, 09:00, 10:00, 14:00, 15:00, 16:00
                    LocalTime[] startTimes = {
                            LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
                            LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0)
                    };
                    
                    for (LocalTime startTime : startTimes) {
                        newSchedules.add(Schedule.builder()
                                .doctorProfile(doctor)
                                .date(currentDate)
                                .startTime(startTime)
                                .endTime(startTime.plusMinutes(30))
                                .isAvailable(true)
                                .build());
                    }
                    currentDate = currentDate.plusDays(1);
                }
            }
        }

        if (!newSchedules.isEmpty()) {
            scheduleRepository.saveAll(newSchedules);
            System.out.println("✅ Đã tạo lịch làm việc tự động cho các bác sĩ trong 30 ngày tới!");
        }
    }
}
