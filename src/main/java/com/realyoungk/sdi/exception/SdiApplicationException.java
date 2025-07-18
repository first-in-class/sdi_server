package com.realyoungk.sdi.exception;

/**
 * SDI 애플리케이션의 모든 커스텀 런타임 예외의 최상위 부모 클래스입니다.
 * 이 예외는 직접 던져지기보다는, 하위 예외들의 타입을 묶어주는 역할을 합니다.
 */
public abstract class SdiApplicationException extends RuntimeException {
    public SdiApplicationException(String message) {
        super(message);
    }

    public SdiApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}