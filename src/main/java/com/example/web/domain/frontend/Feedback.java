package com.example.web.domain.frontend;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1l;

    private String email;
    private String firstName;
    private String lastName;
    private String feedback;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Feedback{");
        sb.append("email='").append(email).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", feedback='").append(feedback).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
