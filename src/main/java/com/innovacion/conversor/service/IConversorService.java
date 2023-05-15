package com.innovacion.conversor.service;

import com.innovacion.conversor.dto.ConvertRequest;
import com.innovacion.conversor.dto.ConvertResponse;
import org.python.core.PyObject;

public interface IConversorService {

    byte[] convert(ConvertRequest request);

}
