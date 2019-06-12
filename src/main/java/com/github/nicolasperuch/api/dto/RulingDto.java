package com.github.nicolasperuch.api.dto;

import javax.validation.constraints.NotNull;

public class RulingDto {

    @NotNull(message = "Your ruling must have a name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RulingDto{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
