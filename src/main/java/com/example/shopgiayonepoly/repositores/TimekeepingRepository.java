package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.Timekeeping;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TimekeepingRepository extends JpaRepository<Timekeeping,Integer> {
    @Query(value = """
    SELECT
        tk.id AS timekeeping_id,                                         --1
        s.id AS staff_id,                                                --2
        s.code_staff + ' - ' + s.full_name AS staff_name,                --3
        tk.time_start_root AS shift_start_time,                               --4
        tk.time_end_root AS shift_end_time,                                   --5
        tk.timekeeping_type,											 --6
        CASE
        WHEN tk.timekeeping_type = 1 THEN N'Chấm công vào'
        WHEN tk.timekeeping_type = 2 THEN N'Chấm công ra'
        END AS timekeeping_type_name,									 --7
        tk.note,														 --8
        FORMAT(tk.create_date, 'HH:mm:ss dd-MM-yyyy') AS formatted_date, --9
        tk.update_date,													--10
        tk.status,														--11
        CASE
        WHEN tk.status = 1 THEN N'Chấm công vào làm đúng giờ'
        WHEN tk.status = 2 THEN N'Chấm công vào làm muộn'
        WHEN tk.status = 3 THEN N'Chấm công ra về sớm'
        WHEN tk.status = 4 THEN N'Chấm công ra về đúng giờ'
        WHEN tk.status = 5 THEN N'Chấm công ra về muộn'
        WHEN tk.status = 6 THEN N'Chấm công vào làm sớm'
        END AS status_name												--12
    FROM
    timekeeping tk
        LEFT JOIN staff s ON tk.id_staff = s.id
        LEFT JOIN shift sh ON s.id_shift = sh.id
    WHERE 
            CONCAT(s.full_name, s.number_phone,s.email,s.code_staff) LIKE CONCAT('%', :searchTerm, '%')
          -- Lọc theo khoảng ngày
             AND tk.create_date BETWEEN :startDate AND :endDate  -- Sửa định dạng ngày
            -- Lọc theo khoảng giờ
            AND CAST(tk.create_date AS TIME) BETWEEN CAST(:startTime AS TIME) AND CAST(:endTime AS TIME)
            AND (:timekeeping_typeCheck IS NULL OR tk.timekeeping_type = :timekeeping_typeCheck)
        ;
""",nativeQuery = true)
    List<Object[]> getAllTimekeeping(
            @Param("searchTerm") String searchTerm,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endtDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime,
            @Param("timekeeping_typeCheck") Integer timekeeping_typeCheck
            );

    //cham cong vao
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO timekeeping (
            id_staff,
            note,
            time_start_root,
            time_end_root,
            timekeeping_type,
            create_date,
            update_date,
            status
        )
        VALUES (
            :idStaff, -- ID nhân viên
            :note, -- Ghi chú
            (SELECT sh.start_time FROM staff s
             LEFT JOIN shift sh ON sh.id = s.id_shift
             WHERE s.id = :idStaff), -- Lấy giờ bắt đầu từ bảng shift
            (SELECT sh.end_time FROM staff s
             LEFT JOIN shift sh ON sh.id = s.id_shift
             WHERE s.id = :idStaff), -- Lấy giờ kết thúc từ bảng shift
            1, -- Chấm công vào (timekeeping_type = 1)
            GETDATE(), -- Thời gian tạo
            GETDATE(), -- Thời gian cập nhật
            -- Xác định trạng thái chấm công vào
            CASE
                WHEN CAST(GETDATE() AS TIME) <
                     (SELECT sh.start_time FROM staff s
                      LEFT JOIN shift sh ON sh.id = s.id_shift
                      WHERE s.id = :idStaff)
                THEN 6
                WHEN CAST(GETDATE() AS TIME) <=
                     DATEADD(MINUTE, 5, (SELECT sh.start_time FROM staff s
                                          LEFT JOIN shift sh ON sh.id = s.id_shift
                                          WHERE s.id = :idStaff))
                THEN 1 -- Chấm vào đúng giờ
                ELSE 2 -- Chấm vào muộn
            END
        );
""",nativeQuery = true)
    void getCheckIn(
            @Param("idStaff") Integer idStaff,
            @Param("note") String note
    );
    //cham cong ra
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO timekeeping (
            id_staff,
            note,
            time_start_root,
            time_end_root,
            timekeeping_type,
            create_date,
            update_date,
            status
        )
        VALUES (
            :idStaff, -- ID nhân viên
            :note, -- Ghi chú
            (SELECT sh.start_time FROM staff s
             LEFT JOIN shift sh ON sh.id = s.id_shift
             WHERE s.id = :idStaff), -- Lấy giờ bắt đầu từ bảng shift
            (SELECT sh.end_time FROM staff s
             LEFT JOIN shift sh ON sh.id = s.id_shift
             WHERE s.id = :idStaff), -- Lấy giờ kết thúc từ bảng shift
            2, -- Chấm công ra (timekeeping_type = 2)
            GETDATE(), -- Thời gian tạo
            GETDATE(), -- Thời gian cập nhật
            -- Xác định trạng thái chấm công ra
            CASE
                WHEN CAST(GETDATE() AS TIME) <
                     (SELECT sh.end_time FROM staff s
                      LEFT JOIN shift sh ON sh.id = s.id_shift
                      WHERE s.id = :idStaff)
                THEN 3 -- Chấm ra sớm
                WHEN CAST(GETDATE() AS TIME) <=
                     DATEADD(MINUTE, 5, (SELECT sh.end_time FROM staff s
                                          LEFT JOIN shift sh ON sh.id = s.id_shift
                                          WHERE s.id = :idStaff))
                THEN 4 -- Chấm ra đúng giờ
                ELSE 5 -- Chấm ra muộn
            END
        );
""",nativeQuery = true)
    void getCheckOut(
            @Param("idStaff") Integer idStaff,
            @Param("note") String note
    );
    @Query(value = """
            SELECT
                CASE
                    WHEN EXISTS (
                        SELECT 1
                        FROM timekeeping tk
                        LEFT JOIN staff s ON tk.id_staff = s.id
                        LEFT JOIN shift sh ON sh.id = s.id_shift
                        WHERE s.id = :idStaff
                            AND tk.time_start_root = sh.start_time
                            AND tk.time_end_root = sh.end_time
                            AND tk.timekeeping_type = :timekeepingTypeCheck
                            AND CONVERT(DATE, tk.create_date) = CONVERT(DATE, GETDATE())
                    ) THEN N'Có'
                    ELSE N'Không'
                END AS KetQua;
""",nativeQuery = true)
    List<Object[]> getCheckStaffAttendanceYet(
            @Param("idStaff") Integer idStaff,
            @Param("timekeepingTypeCheck") Integer timekeepingTypeCheck
    );

    @Query(value = """
    	SELECT
        CASE
            -- Kiểm tra nếu có điểm danh ra (timekeeping_type = 2)
            WHEN EXISTS (
                SELECT 1
                FROM timekeeping tk_out
                LEFT JOIN staff s ON tk_out.id_staff = s.id
                WHERE s.id = 1
                    AND tk_out.timekeeping_type = 2 -- Loại điểm danh ra
                    AND CONVERT(DATE, tk_out.create_date) = CONVERT(DATE, GETDATE()) -- Cùng ngày
            ) THEN N'Đã ra về'
            -- Kiểm tra nếu chưa có điểm danh ra và đã quá 90 phút
            WHEN EXISTS (
                SELECT 1
                FROM timekeeping tk_in
                LEFT JOIN staff s ON tk_in.id_staff = s.id
                LEFT JOIN shift sh ON sh.id = s.id_shift
                WHERE s.id = 1
                    AND tk_in.timekeeping_type = 1 -- Loại điểm danh vào
                    AND tk_in.time_start_root = sh.start_time
                    AND tk_in.time_end_root = sh.end_time
                    AND CONVERT(DATE, tk_in.create_date) = CONVERT(DATE, GETDATE()) -- Cùng ngày
                    AND GETDATE() > DATEADD(MINUTE, 90, CAST(CONCAT(CONVERT(DATE, GETDATE()), ' ', tk_in.time_end_root) AS DATETIME)) -- Vượt 90 phút
            ) THEN N'Khẩn cấp'
            -- Nếu không có vấn đề gì thì báo "Vẫn ổn"
            ELSE N'Vẫn ổn'
        END AS KetQua;
""",nativeQuery = true)
    List<Object[]> getCheckStaffCheckOut(
            @Param("idStaff") Integer idStaff
    );

    @Query(value = """
    SELECT
        tk.id AS timekeeping_id,                                         --1
        s.id AS staff_id,                                                --2
        s.code_staff + ' - ' + s.full_name AS staff_name,                --3
        tk.time_start_root AS shift_start_time,                               --4
        tk.time_end_root AS shift_end_time,                                   --5
        tk.timekeeping_type,											 --6
        CASE
        WHEN tk.timekeeping_type = 1 THEN N'Chấm công vào'
        WHEN tk.timekeeping_type = 2 THEN N'Chấm công ra'
        END AS timekeeping_type_name,									 --7
        tk.note,														 --8
        FORMAT(tk.create_date, 'HH:mm:ss dd-MM-yyyy') AS formatted_date, --9
        tk.update_date,													--10
        tk.status,														--11
        CASE
        WHEN tk.status = 1 THEN N'Chấm công vào làm đúng giờ'
        WHEN tk.status = 2 THEN N'Chấm công vào làm muộn'
        WHEN tk.status = 3 THEN N'Chấm công ra về sớm'
        WHEN tk.status = 4 THEN N'Chấm công ra về đúng giờ'
        WHEN tk.status = 5 THEN N'Chấm công ra về muộn'
        WHEN tk.status = 6 THEN N'Chấm công vào làm sớm'
        END AS status_name												--12
    FROM
    timekeeping tk
        LEFT JOIN staff s ON tk.id_staff = s.id
        LEFT JOIN shift sh ON s.id_shift = sh.id
    WHERE 
            tk.id_staff = :idStaffCheck 
          -- Lọc theo khoảng ngày
             AND tk.create_date BETWEEN :startDate AND :endDate  -- Sửa định dạng ngày
            -- Lọc theo khoảng giờ
            AND CAST(tk.create_date AS TIME) BETWEEN CAST(:startTime AS TIME) AND CAST(:endTime AS TIME)
            order by tk.create_date desc 
        ;
""",nativeQuery = true)
    List<Object[]> getAllTimekeepingByIdStaff(
            @Param("idStaffCheck") Integer idStaffCheck,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endtDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

}
