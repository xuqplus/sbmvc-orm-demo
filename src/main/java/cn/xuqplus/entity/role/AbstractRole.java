package cn.xuqplus.entity.role;

import cn.xuqplus.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractRole extends AbstractEntity {
    @Column(columnDefinition = "enum('unknown','root','admin','normal')")
    private Name name;

    public enum Name {
        unknown(0, "未知"), root(1, "超级管理员"), admin(2, "管理员"), normal(3, "普通用户");

        private Integer id;
        private String name;

        Name(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

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
    }
}
