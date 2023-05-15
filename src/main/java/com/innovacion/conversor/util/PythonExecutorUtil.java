package com.innovacion.conversor.util;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class PythonExecutorUtil {
    public static PyObject executePythonFunction(String functionName, Object... args) {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.execfile("src/main/resources/script.py");

            PyObject pyFunction = interpreter.get(functionName);
            if (pyFunction != null && pyFunction.isCallable()) {
                PyObject[] pyArgs = new PyObject[args.length];
                for (int i = 0; i < args.length; i++) {
                    pyArgs[i] = Py.java2py(args[i]);
                }
                return pyFunction.__call__(pyArgs);
            }
        }
        return null;
    }
}