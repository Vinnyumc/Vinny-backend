package com.vinny.backend.error.code.status;

import com.vinny.backend.error.code.BaseErrorCode;
import com.vinny.backend.error.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),


    // 멤버 관려 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER4001", "사용자가 없습니다."),

    // 예시,,,
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE4001", "게시글이 없습니다."),

    // page
    INVALID_PAGE_PARAM(HttpStatus.BAD_REQUEST, "PAGEPARAM4001", "page는 1 이상의 정수여야 합니다."),
    MISSING_PAGE_PARAM(HttpStatus.BAD_REQUEST, "PAGEPARAM4002", "page 파라미터가 필요합니다."),
    INVALID_PAGE_FORMAT(HttpStatus.BAD_REQUEST, "PAGEPARAM4003", "page는 숫자여야 합니다."),

    // S3 관련 에러
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_ERROR_5001", "S3 파일 업로드에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_ERROR_5002", "S3 파일 삭제에 실패했습니다."),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "S3_ERROR_4001", "업로드할 파일이 없습니다."),
    INVALID_FILE_URL(HttpStatus.BAD_REQUEST, "S3_ERROR_4002", "잘못된 형식의 파일 URL입니다."),

    SHOP_NOT_FOUND(HttpStatus.BAD_REQUEST, "SHOP4001", "존재하지 않는 Shop입니다."),


    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}