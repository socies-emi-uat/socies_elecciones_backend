package com.socies.voto.utils;

public class ResponseWrapper<T> {
  private int status;
  private boolean success;
  private String message;
  private T data;

  public ResponseWrapper(boolean success, String message, T data) {
    this.success = success;
    this.message = message;
    this.data = data;
    this.status = 200;
  }

  // Getters y Setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public int getStatus() {
    return this.status;
  }
}
