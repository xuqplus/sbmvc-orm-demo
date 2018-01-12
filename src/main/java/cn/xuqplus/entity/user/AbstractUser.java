package cn.xuqplus.entity.user;

import cn.xuqplus.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractUser extends AbstractEntity {

    private String name;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('unknown','male','female','other')")
    private Sex sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public enum Sex {
        unknown(0, "未知"), male(1, "男"), female(2, "女"), other(3, "其他");

        private Integer id;
        private String name;

        Sex(Integer i, String name) {
            this.id = i;
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
