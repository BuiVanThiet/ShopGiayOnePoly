package com.example.shopgiayonepoly.repositores;

import com.example.shopgiayonepoly.entites.CashierInventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CashierInventoryRepository extends JpaRepository<CashierInventory,Integer> {

    @Query(value = """
    select
        cain.id,
        s.id,
        s.code_staff + ' - ' + s.full_name as info_staff,
        cain.start_time_root,
        cain.time_end_root,
        cain.total_money_bill,
        cain.total_money_return_bill,
        cain.total_money_exchange_bill
    from cashier_inventory cain
        left join staff s on s.id = cain.id_staff
    where 
        cain.create_date BETWEEN :startDate AND :endDate
    and
        CONCAT(s.full_name, s.number_phone,s.email,s.code_staff) LIKE CONCAT('%', :keyStaff, '%')
 
""",nativeQuery = true)
    List<Object[]> getAllCashierInventoryByStaff(
            @Param("keyStaff")String keyStaff,
            @Param("startDate")Date startDate,
            @Param("endDate")Date endDate
    );

    @Query(value = """
           SELECT
            CASE
                WHEN EXISTS (
                    SELECT 1
                    FROM cashier_inventory cain
                    LEFT JOIN staff s ON cain.id_staff = s.id
                    LEFT JOIN shift sh ON sh.id = s.id_shift
                    WHERE s.id = :idStaff
                        AND cain.start_time_root = sh.start_time
                        AND cain.time_end_root = sh.end_time
                        AND CONVERT(DATE, cain.create_date) = CONVERT(DATE, GETDATE())
                ) THEN N'Có'
                ELSE N'Không'
            END AS KetQua;
""",nativeQuery = true)
    List<Object[]> getCheckCashierInventoryStaff(
            @Param("idStaff") Integer idStaff
    );

    @Modifying
    @Transactional
    @Query(value = """
        insert into cashier_inventory(
        	id_staff,
        	start_time_root,
        	time_end_root,
        	total_money_bill,
        	total_money_return_bill,
        	total_money_exchange_bill,
        	create_date,
        	update_date,
        	status) values(
        		:idStaff ,
        		(select sh.start_time from staff s left join shift sh on sh.id = s.id_shift where s.id = :idStaff),
        		(select sh.end_time from staff s left join shift sh on sh.id = s.id_shift where s.id = :idStaff),
        		:totalMoneyBill ,
        		:totalMoneyReturnBill ,
        		:totalMoneyExchangeBill ,
        		GETDATE(),
        		GETDATE(),
        		1
        	)
""",nativeQuery = true)
    void getInsertRevenue(
            @Param("idStaff") Integer idStaff,
            @Param("totalMoneyBill") BigDecimal totalMoneyBill,
            @Param("totalMoneyReturnBill") BigDecimal totalMoneyReturnBill,
            @Param("totalMoneyExchangeBill") BigDecimal totalMoneyExchangeBill
            );
    @Modifying
    @Transactional
    @Query(value = """
    update 
        cashier_inventory 
    set 
        total_money_bill = total_money_bill + :totalMoneyBill , 
        total_money_return_bill = total_money_return_bill + :totalMoneyReturnBill , 
        total_money_exchange_bill= total_money_exchange_bill + :totalMoneyExchangeBill, 
        update_date = getdate() 
    where 
        id_staff = :idStaff
""",nativeQuery = true)
    void getUpdateRevenue(
            @Param("idStaff") Integer idStaff,
            @Param("totalMoneyBill") BigDecimal totalMoneyBill,
            @Param("totalMoneyReturnBill") BigDecimal totalMoneyReturnBill,
            @Param("totalMoneyExchangeBill") BigDecimal totalMoneyExchangeBill
    );


    // cho ben ho so xem
    @Query(value = """
    select
        cain.id,
        s.id,
        s.code_staff + ' - ' + s.full_name as info_staff,
        cain.start_time_root,
        cain.time_end_root,
        cain.total_money_bill,
        cain.total_money_return_bill,
        cain.total_money_exchange_bill,
        FORMAT( cain.create_date, 'HH:mm:ss dd-MM-yyyy') AS formatted_date
    from cashier_inventory cain
        left join staff s on s.id = cain.id_staff
    where 
        cain.create_date BETWEEN :startDate AND :endDate
        and CONVERT(TIME, cain.create_date) BETWEEN :startTime AND :endTime
    and
        s.id = :idStaff
 
""",nativeQuery = true)
    List<Object[]> getAllCashierInventoryByIdStaff(
            @Param("idStaff")Integer idStaff,
            @Param("startDate")Date startDate,
            @Param("endDate")Date endDate,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );


}
