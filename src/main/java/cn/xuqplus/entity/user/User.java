package cn.xuqplus.entity.user;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User extends AbstractUser {
}
