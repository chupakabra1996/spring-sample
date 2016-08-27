package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;

@Repository
public interface RegisterTokensRepository extends JpaRepository<RegisterVerificationToken, Long> {

    RegisterVerificationToken findByToken(String token);
}
