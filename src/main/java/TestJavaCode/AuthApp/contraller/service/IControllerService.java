package TestJavaCode.AuthApp.contraller.service;

import TestJavaCode.AuthApp.dto.requestDTO.AuthLoginPasswordRequestDTO;

public interface IControllerService {

    public void blockUser(AuthLoginPasswordRequestDTO authRequest);
}
