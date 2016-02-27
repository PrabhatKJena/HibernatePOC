package edu.pk.poc.orm.entity;

import edu.pk.poc.orm.core.annotaion.ORMColumn;
import edu.pk.poc.orm.core.annotaion.ORMEntity;

@ORMEntity(tableName = "DEPT")
public class DepartmentEntity {
    @ORMColumn(name = "DEPTID", isId = true)
    Integer deptId;
    @ORMColumn(name = "DEPTNAME")
    String deptName;
    @ORMColumn(name = "LOCATION")
    String location;

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
