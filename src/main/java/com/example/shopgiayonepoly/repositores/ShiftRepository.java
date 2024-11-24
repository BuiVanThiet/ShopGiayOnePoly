package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift,Integer> {
    @Query(value = """
    SELECT
        id,
        start_time,
        end_time,
        CASE
            WHEN CONVERT(time, GETDATE()) < start_time THEN 1 -- CHƯA BẮT ĐẦU
            WHEN CONVERT(time, GETDATE()) BETWEEN start_time AND end_time THEN 2 -- ĐANG BẮT ĐẦU
            WHEN CONVERT(time, GETDATE()) > end_time THEN 3 -- ĐÃ KẾT THÚC
            ELSE 0 -- KHÔNG XÁC ĐỊNH
        END AS shift_status,
        create_date,
        update_date,
        status
    FROM shift
    WHERE (:startTimeBegin IS NULL OR start_time BETWEEN :startTimeBegin AND :startTimeEnd)
          AND (:endTimeBegin IS NULL OR end_time BETWEEN :endTimeBegin AND :endTimeEnd)
            AND (:statusShift IS NULL OR
                        CASE
                            WHEN CONVERT(time, GETDATE()) < start_time THEN 1 -- CHƯA BẮT ĐẦU
                            WHEN CONVERT(time, GETDATE()) BETWEEN start_time AND end_time THEN 2 -- ĐANG BẮT ĐẦU
                            WHEN CONVERT(time, GETDATE()) > end_time THEN 3 -- ĐÃ KẾT THÚC
                            ELSE 0 -- KHÔNG XÁC ĐỊNH
                        END = :statusShift)
          AND (:status IS NULL OR status = :status) 
    order by update_date desc
""", nativeQuery = true)
    List<Object[]> getAllShiftByTime(
            @Param("startTimeBegin") String startTimeBegin,
            @Param("startTimeEnd") String startTimeEnd,
            @Param("endTimeBegin") String endTimeBegin,
            @Param("endTimeEnd") String endTimeEnd,
            @Param("statusShift") Integer statusShift,
            @Param("status") Integer status
    );
    @Query(value = """

            SELECT
        s.id,
        s.code_staff,
        s.full_name,
        s.number_phone,
        s.email,
        sh.start_time,
        sh.end_time,
        sh_virtual.start_time AS sh_virtual_start_time,
        sh_virtual.end_time AS sh_virtual_end_time,
        CASE
            WHEN CONVERT(TIME, GETDATE()) < sh.start_time THEN 1 -- CHƯA BẮT ĐẦU
            WHEN CONVERT(TIME, GETDATE()) BETWEEN sh.start_time AND sh.end_time THEN 2 -- ĐANG BẮT ĐẦU
            WHEN CONVERT(TIME, GETDATE()) > sh.end_time THEN 3 -- ĐÃ KẾT THÚC
            ELSE 0 -- KHÔNG XÁC ĐỊNH
        END AS shift_status,
        sh.status,
         CASE
                -- Kiểm tra nếu đã điểm danh vào và điểm danh ra về
                WHEN EXISTS (
                    SELECT 1
                    FROM timekeeping tk
                    WHERE tk.id_staff = s.id
                      AND tk.timekeeping_type = 1 -- Điểm danh vào
                      AND tk.create_date >= CAST(GETDATE() AS DATE)
                      AND tk.create_date < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    AND tk.time_start_root = sh.start_time
                    AND tk.time_end_root = sh.end_time
                )
                AND EXISTS (
                    SELECT 1
                    FROM timekeeping tk
                    WHERE tk.id_staff = s.id
                      AND tk.timekeeping_type = 2 -- Điểm danh ra về
                      AND tk.create_date >= CAST(GETDATE() AS DATE)
                      AND tk.create_date < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    AND tk.time_start_root = sh.start_time
                    AND tk.time_end_root = sh.end_time
                ) THEN N'Đã hoàn tất'
        
                -- Kiểm tra nếu chỉ điểm danh vào và thời gian khớp
                WHEN EXISTS (
                    SELECT 1
                    FROM timekeeping tk
                    WHERE tk.id_staff = s.id
                      AND tk.timekeeping_type = 1 -- Điểm danh vào
                      AND tk.create_date >= CAST(GETDATE() AS DATE)
                      AND tk.create_date < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    AND tk.time_start_root = sh.start_time
                    AND tk.time_end_root = sh.end_time
                ) THEN N'Đã điểm danh vào'
        
                -- Nếu điểm danh không đúng ca
                WHEN EXISTS (
                    SELECT 1
                    FROM timekeeping tk
                    WHERE tk.id_staff = s.id
                      AND tk.timekeeping_type = 1 -- Điểm danh vào
                      AND tk.create_date >= CAST(GETDATE() AS DATE)
                      AND tk.create_date < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
                    AND (tk.time_start_root != sh.start_time OR tk.time_end_root != sh.end_time)
                ) THEN N'Đang chờ'
        
                ELSE N'Đang chờ'
            END AS attendance_status
    FROM staff s
        CROSS JOIN (
            SELECT start_time, end_time
            FROM shift
            WHERE id = :idShiftCheck
        ) sh_virtual
    LEFT JOIN shift sh ON sh.id = s.id_shift
    where s.status = 1
        and CONCAT(s.full_name, s.number_phone,s.email,s.code_staff) LIKE CONCAT('%', :searchTerm, '%')
        and (:checkShift = 2 AND id_shift IS NOT NULL)
               OR (:checkShift = 1 AND id_shift IS NULL)
    order by s.update_date desc
""",nativeQuery = true)
    List<Object[]> getAllStaffByShift(
            @Param("idShiftCheck") Integer idShiftCheck,
            @Param("searchTerm") String searchTerm,
            @Param("checkShift") Integer checkShift
            );
}
