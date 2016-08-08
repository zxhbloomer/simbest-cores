package com.simbest.cores.admin.authority.model;

import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.ReferenceTable;
import com.simbest.cores.utils.annotations.ReferenceTables;
import com.simbest.cores.utils.annotations.Unique;

import javax.persistence.*;

@Entity
@Table(name = "sys_group")
@ReferenceTables(joinTables = {@ReferenceTable(table = "sys_user_grop", value = "用户与组")})
public class SysGroup extends GenericModel<SysGroup> {

    private static final long serialVersionUID = -425891999687281986L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    @Unique
    private String name;

    @Column(name = "type", columnDefinition = "VARCHAR(200) default 'assignment'", nullable = false, length = 50)
    private String type;

    @Column(name = "description")
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}