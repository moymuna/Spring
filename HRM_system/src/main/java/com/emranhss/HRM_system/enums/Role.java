package com.emranhss.HRM_system.enums;

public enum Role {
  ADMIN,
  HR,
  MANAGER,
  EMPLOYEE,
  APPLICANT;

  public String getAuthority() {
    return "ROLE_" + this.name();
  }
}
