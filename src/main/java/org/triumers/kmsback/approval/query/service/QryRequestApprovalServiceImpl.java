package org.triumers.kmsback.approval.query.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triumers.kmsback.approval.query.dto.QryRequestApprovalInfoDTO;
import org.triumers.kmsback.approval.query.dto.QryRequestApprovalWithEmployeeDTO;
import org.triumers.kmsback.approval.query.repository.QryRequestApprovalMapper;
import org.triumers.kmsback.employee.command.Application.dto.CmdEmployeeDTO;
import org.triumers.kmsback.employee.command.Application.service.CmdEmployeeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QryRequestApprovalServiceImpl implements QryRequestApprovalService{

    private final QryRequestApprovalMapper qryRequestApprovalMapper;

    // 직원 조회 따로
    private final CmdEmployeeService cmdEmployeeService;

    @Autowired
    public QryRequestApprovalServiceImpl(QryRequestApprovalMapper qryRequestApprovalMapper, CmdEmployeeService cmdEmployeeService) {
        this.qryRequestApprovalMapper = qryRequestApprovalMapper;
        this.cmdEmployeeService = cmdEmployeeService;
    }

    // 본인이 요청한 결재 단일 조회
    public QryRequestApprovalWithEmployeeDTO findById(int requesterId, int approvalId) {

        QryRequestApprovalInfoDTO approvalInfo = qryRequestApprovalMapper.findById(requesterId, approvalId);

        // 결재 요청자(requester)의 모든 정보 조회
        CmdEmployeeDTO requester = cmdEmployeeService.findEmployeeById(approvalInfo.getRequesterId());

        // 결재자(approver)의 모든 정보 조회
        CmdEmployeeDTO approver = cmdEmployeeService.findEmployeeById(approvalInfo.getApproverId());

        QryRequestApprovalWithEmployeeDTO result = new QryRequestApprovalWithEmployeeDTO();
        result.setApprovalInfo(approvalInfo);
        result.setRequester(requester);
        result.setApprover(approver);

        return result;
    }

    // 본인이 요청한 결재 전체 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findAll(int requesterId, int page, int size) {

        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findAll(requesterId, offset, limit);

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 유형별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findByType(int requesterId, int typeId, int page, int size) {
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByType(requesterId, typeId, offset, limit);

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 기간별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findByDateRange(int requesterId, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {

        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByDateRange(requesterId, startDate, endDate, offset, limit);

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }


    private List<QryRequestApprovalInfoDTO> getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(List<QryRequestApprovalInfoDTO> approvalInfoDTOS) {
        for (QryRequestApprovalInfoDTO approvalInfoDTO : approvalInfoDTOS) {

            // 결재 요청자(requester)의 이름과 아이디 조회
            CmdEmployeeDTO requester = cmdEmployeeService.findEmployeeById(approvalInfoDTO.getRequesterId());
            approvalInfoDTO.setRequesterId(requester.getId());
            approvalInfoDTO.setRequesterName(requester.getName());

            // 결재자(approver)의 이름과 아이디 조회
            CmdEmployeeDTO approver = cmdEmployeeService.findEmployeeById(approvalInfoDTO.getApproverId());
            approvalInfoDTO.setApproverId(approver.getId());
            approvalInfoDTO.setApproverName(approver.getName());
        }

        return approvalInfoDTOS;
    }
}
