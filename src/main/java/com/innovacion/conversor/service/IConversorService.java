package com.innovacion.conversor.service;

import com.innovacion.conversor.dto.ConvertRequest;
import com.innovacion.conversor.dto.ConvertResponse;
import org.python.core.PyObject;

public interface IConversorService {

    PyObject convert(ConvertRequest request);

}
