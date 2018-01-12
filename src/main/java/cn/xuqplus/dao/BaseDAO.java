package cn.xuqplus.dao;

import cn.xuqplus.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDAO<T extends AbstractEntity, ID extends Serializable> extends JpaRepository<T, ID> {
}

