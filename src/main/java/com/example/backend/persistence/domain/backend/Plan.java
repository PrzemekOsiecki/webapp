package com.example.backend.persistence.domain.backend;

import com.example.enums.PlansEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id;
    private String name;

    public Plan(PlansEnum planEnum) {
        this.id = planEnum.getId();
        this.name = planEnum.getPlanName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return id == plan.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
