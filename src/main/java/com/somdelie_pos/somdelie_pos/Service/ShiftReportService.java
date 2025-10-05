package com.somdelie_pos.somdelie_pos.Service;

import com.somdelie_pos.somdelie_pos.payload.dto.ShiftReportDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShiftReportService {

    ShiftReportDTO startShift() throws Exception;

    ShiftReportDTO endShift(UUID shiftReportId, LocalDateTime shiftEnd) throws Exception;

    ShiftReportDTO getShiftReportById(UUID shiftReportId) throws Exception;

    List<ShiftReportDTO> getAllShiftReports();

    List<ShiftReportDTO> getShiftReportsByBranchId(UUID branchId) throws Exception;

    List<ShiftReportDTO>getShiftReportsByCashierId(UUID cashierId) throws Exception;

    ShiftReportDTO getCurrentShiftProgress(UUID cashierId) throws Exception;


    ShiftReportDTO getShiftByCashierAndDate(UUID cashierId, LocalDateTime date) throws Exception;



}
