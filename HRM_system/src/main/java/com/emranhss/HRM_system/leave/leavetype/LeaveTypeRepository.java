package com.emranhss.HRM_system.leave.leavetype;

import com.emranhss.HRM_system.enums.LeavesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveTypeRepository  extends JpaRepository<LeaveType,Long> {

    Optional<LeaveType> findByName(LeavesType name);

    boolean existsByName(LeavesType name);
}
