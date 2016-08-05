package com.simbest.cores.admin.authority.model;

import com.simbest.cores.model.GenericModel;
import com.simbest.cores.utils.annotations.ReferenceTable;
import com.simbest.cores.utils.annotations.ReferenceTables;
import com.simbest.cores.utils.annotations.Unique;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity
//@Table(name = "act_id_group")
//@ReferenceTables(joinTables = {@ReferenceTable(table = "act_id_membership", value = "用户与组")})
public class SysGroup extends GenericModel<SysGroup> {

    private static final long serialVersionUID = -425891999687281986L;

    @Id
    @Column(name = "id_")
    private String id_;

    @Column(name = "name_", nullable = false, unique = true, length = 100)
    @Unique
    private String name_;

    @Column(name = "type_", nullable = false, length = 50)
    private String type_;

    @Column(name = "rev_")
    private Integer rev_;

    public String getId_() {
        return id_;
    }

    public void setId_(String id_) {
        this.id_ = id_;
    }

    public String getName_() {
        return name_;
    }

    public void setName_(String name_) {
        this.name_ = name_;
    }

    public String getType_() {
        return type_;
    }

    public void setType_(String type_) {
        this.type_ = type_;
    }

    public Integer getRev_() {
        return rev_;
    }

    public void setRev_(Integer rev_) {
        this.rev_ = rev_;
    }
}