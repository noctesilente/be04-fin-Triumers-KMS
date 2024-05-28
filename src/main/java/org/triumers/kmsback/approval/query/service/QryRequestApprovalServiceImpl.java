package org.triumers.kmsback.approval.query.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.triumers.kmsback.approval.query.dto.QryRequestApprovalInfoDTO;
import org.triumers.kmsback.approval.query.dto.QryRequestApprovalWithEmployeeDTO;
import org.triumers.kmsback.approval.query.repository.QryRequestApprovalMapper;
import org.triumers.kmsback.common.exception.NotLoginException;
import org.triumers.kmsback.user.command.Application.dto.CmdEmployeeDTO;
import org.triumers.kmsback.user.command.Application.service.AuthService;
import org.triumers.kmsback.user.command.Application.service.CmdEmployeeService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QryRequestApprovalServiceImpl implements QryRequestApprovalService {

    private final QryRequestApprovalMapper qryRequestApprovalMapper;
    private final CmdEmployeeService cmdEmployeeService;
    private final AuthService authService;

    @Autowired
    public QryRequestApprovalServiceImpl(QryRequestApprovalMapper qryRequestApprovalMapper, CmdEmployeeService cmdEmployeeService, AuthService authService) {
        this.qryRequestApprovalMapper = qryRequestApprovalMapper;
        this.cmdEmployeeService = cmdEmployeeService;
        this.authService = authService;
    }

    // 본인이 요청한 결재 단일 조회
    public QryRequestApprovalWithEmployeeDTO findById(int approvalId) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        QryRequestApprovalInfoDTO approvalInfo = qryRequestApprovalMapper.findById(requesterId, approvalId);
        if (approvalInfo == null) {
            throw new IllegalArgumentException("Approval not found for requesterId: " + requesterId + ", approvalId: " + approvalId);
        }

        return getQryRequestApprovalWithEmployeeDTO(approvalInfo);
    }

    // 본인이 요청한 결재 전체 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findAll(int page, int size) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findAll(requesterId, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for requesterId: " + requesterId);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 유형별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findByType(int typeId, int page, int size) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByType(requesterId, typeId, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for requesterId: " + requesterId + ", typeId: " + typeId);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 기간별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByDateRange(requesterId, startDate, endDate, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for requesterId: " + requesterId + " between " + startDate + " and " + endDate);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청받은 결재 단일 조회
    public QryRequestApprovalWithEmployeeDTO findReceivedById(int requestApprovalId) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        QryRequestApprovalInfoDTO approvalInfo = qryRequestApprovalMapper.findReceivedById(approverId, requestApprovalId);
        if (approvalInfo == null) {
            throw new IllegalArgumentException("Approval not found for approverId: " + approverId + ", requestApprovalId: " + requestApprovalId);
        }

        return getQryRequestApprovalWithEmployeeDTO(approvalInfo);
    }

    // 본인이 요청받은 결재 전체 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findAllReceived(int page, int size) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findAllReceived(approverId, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for approverId: " + approverId);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청받은 결재 유형별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findReceivedByType(int typeId, int page, int size) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findReceivedByType(approverId, typeId, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for approverId: " + approverId + ", typeId: " + typeId);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청받은 결재 기간별 조회(페이징 처리)
    public List<QryRequestApprovalInfoDTO> findReceivedByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findReceivedByDateRange(approverId, startDate, endDate, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for approverId: " + approverId + " between " + startDate + " and " + endDate);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 내용 검색
    public List<QryRequestApprovalInfoDTO> findByContent(String keyword, int page, int size) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByContent(requesterId, keyword, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for requesterId: " + requesterId + ", keyword: " + keyword);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청한 결재 승인 상태별 조회
    public List<QryRequestApprovalInfoDTO> findByStatus(String status, int page, int size) throws NotLoginException {
        int requesterId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findByStatus(requesterId, status, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for requesterId: " + requesterId + ", status: " + status);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청받은 결재 내용 검색
    public List<QryRequestApprovalInfoDTO> findReceivedByContent(String keyword, int page, int size) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findReceivedByContent(approverId, keyword, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for approverId: " + approverId + ", keyword: " + keyword);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    // 본인이 요청받은 결재 승인 상태별 조회
    public List<QryRequestApprovalInfoDTO> findReceivedByStatus(String status, int page, int size) throws NotLoginException {
        int approverId = authService.whoAmI().getId();
        int offset = (page - 1) * size;
        int limit = size;

        List<QryRequestApprovalInfoDTO> approvalInfoDTOS = qryRequestApprovalMapper.findReceivedByStatus(approverId, status, offset, limit);
        if (approvalInfoDTOS == null || approvalInfoDTOS.isEmpty()) {
            throw new IllegalArgumentException("No approvals found for approverId: " + approverId + ", status: " + status);
        }

        return getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(approvalInfoDTOS);
    }

    private QryRequestApprovalWithEmployeeDTO getQryRequestApprovalWithEmployeeDTO(QryRequestApprovalInfoDTO approvalInfo) {
        CmdEmployeeDTO requester = cmdEmployeeService.findEmployeeById(approvalInfo.getRequesterId());
        if (requester == null) {
            throw new IllegalArgumentException("Requester not found with id: " + approvalInfo.getRequesterId());
        }

        CmdEmployeeDTO approver = cmdEmployeeService.findEmployeeById(approvalInfo.getApproverId());
        if (approver == null) {
            throw new IllegalArgumentException("Approver not found with id: " + approvalInfo.getApproverId());
        }

        QryRequestApprovalWithEmployeeDTO result = new QryRequestApprovalWithEmployeeDTO();
        result.setApprovalInfo(approvalInfo);
        result.setRequester(requester);
        result.setApprover(approver);

        return result;
    }

    private List<QryRequestApprovalInfoDTO> getQryRequestApprovalInfoDTOSWithEmployeeIdAndName(List<QryRequestApprovalInfoDTO> approvalInfoDTOS) {
        for (QryRequestApprovalInfoDTO approvalInfoDTO : approvalInfoDTOS) {
            CmdEmployeeDTO requester = cmdEmployeeService.findEmployeeById(approvalInfoDTO.getRequesterId());
            if (requester == null) {
                throw new IllegalArgumentException("Requester not found with id: " + approvalInfoDTO.getRequesterId());
            }
            approvalInfoDTO.setRequesterId(requester.getId());
            approvalInfoDTO.setRequesterName(requester.getName());

            CmdEmployeeDTO approver = cmdEmployeeService.findEmployeeById(approvalInfoDTO.getApproverId());
            if (approver == null) {
                throw new IllegalArgumentException("Approver not found with id: " + approvalInfoDTO.getApproverId());
            }
            approvalInfoDTO.setApproverId(approver.getId());
            approvalInfoDTO.setApproverName(approver.getName());
        }

        return approvalInfoDTOS;
    }
}