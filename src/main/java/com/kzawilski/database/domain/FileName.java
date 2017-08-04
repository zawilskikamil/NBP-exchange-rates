package com.kzawilski.database.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "FILE_NAMES")
@NamedQueries({
        @NamedQuery(name = "getAllNames", query = "select e from FileName e")
})
@Access(AccessType.FIELD)
public class FileName implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "FILE_NAME")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
