package org.triumers.kmsback.post.query.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.triumers.kmsback.common.exception.NotLoginException;
import org.triumers.kmsback.user.command.Application.dto.CmdEmployeeDTO;
import org.triumers.kmsback.post.query.aggregate.vo.QryRequestPost;
import org.triumers.kmsback.post.query.dto.QryPostAndTagsDTO;

import java.util.List;

public interface QryPostService {

    Page<QryPostAndTagsDTO> findPostListByTab(QryRequestPost request, Pageable pageable) throws NotLoginException;

    Page<QryPostAndTagsDTO> findAllPostListByEmployee(QryRequestPost request, Pageable pageable) throws NotLoginException;

    QryPostAndTagsDTO findPostById(int postId) throws NotLoginException;

    List<QryPostAndTagsDTO> findHistoryListByOriginId(int originId) throws NotLoginException;

    List<CmdEmployeeDTO> findLikeListByPostId(int postId);

    Boolean getIsEditingById(int postId);

    List<QryPostAndTagsDTO> findPostByEmployeeId(int employeeId);

    List<QryPostAndTagsDTO> findLikePostByEmployeeId(int employeeId);

    List<QryPostAndTagsDTO> findFavoritePostByEmployeeId(int employeeId);

    Boolean findIsLikedByPostId(int postId) throws NotLoginException;

    Boolean findIsFavoriteByPostId(int postId) throws NotLoginException;
}
