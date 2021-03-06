package com.example.demo.service.impl;

import com.example.demo.model.AyUser;
import com.example.demo.repository.AyUserRepository;
import com.example.demo.service.AyUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务层实现类
 * @Author: Carl
 * @Date: 2018/9/17 16:04
 * @Version 1.0
 */
//注解在类上
@Transactional
@Service
public class AyUserServiceImpl implements AyUserService{

    @Resource(name="ayUserRepository")
    private AyUserRepository ayUserRepository;

    @Override
    public Optional<AyUser> findById(String id) {
        return ayUserRepository.findById(id);
    }

    @Override
    public List<AyUser> findAll() {
        return ayUserRepository.findAll();
    }

  // 注解在方法上
  @Transactional
  @Override
  public AyUser save(AyUser ayUser) {

    // return ayUserRepository.save(ayUser);

    // 出现空指针异常
    AyUser saveUser = ayUserRepository.save(ayUser);
    String error = null;
    error.split("/");
    return saveUser;
  }

    @Override
    public void delete(String id) {
            ayUserRepository.deleteById(id);
    }

    /**
     * 分页
     * @param pageable
     * @return
     */
    @Override
    public Page<AyUser> findAll(Pageable pageable) {
        return ayUserRepository.findAll(pageable);
    }

    @Override
    public List<AyUser> findByName(String name) {
        return ayUserRepository.findByName(name);
    }

    @Override
    public List<AyUser> findByNameLike(String name) {
        return ayUserRepository.findByNameLike(name);
    }

    @Override
    public List<AyUser> findByIdIn(Collection<String> ids) {
        return ayUserRepository.findByIdIn(ids);
    }
}
