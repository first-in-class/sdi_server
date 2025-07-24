package com.realyoungk.sdi.exception;

/**
 * 'Visit' 데이터를 조회하는 비즈니스 로직 수행 중 실패했을 때 던져지는 예외입니다.
 * 이 이름은 우리 애플리케이션의 핵심 도메인인 'Visit'과 직접적으로 연관됩니다.
 */
public class VisitFetchException extends SdiApplicationException {
    public VisitFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}