package com.core.api.application.validators;

import java.util.ArrayList;
import java.util.List;

public abstract class Validator {
    protected List<String> warningErrors;
    protected boolean valid = true;

    public Validator() {
        warningErrors = new ArrayList<>();
    }

    protected abstract void validate();

    protected void addWarning(String warning) {
        if (warning != null && !warning.trim().isEmpty()) {
            warningErrors.add(warning.trim());
        }
    }

    public String getWarnings() {
        StringBuilder message = new StringBuilder("Se encontraron los siguientes detalles: \n");
        warningErrors.forEach(warning -> message.append(warning).append(" \n"));
        return message.toString();
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}