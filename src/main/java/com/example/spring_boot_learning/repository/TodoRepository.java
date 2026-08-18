package com.example.spring_boot_learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.spring_boot_learning.entity.Todo;

public interface TodoRepository 
    extends JpaRepository<Todo, Long> {
}
// <管理する型, IDの型>
// extends: interfaceが別のinterfaceを引き継ぐ
// 今回はJpaRepositoryを引き継ぐ
// 実装クラスを書かなくていい

// DB操作の窓口