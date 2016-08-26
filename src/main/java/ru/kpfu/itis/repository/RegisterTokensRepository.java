package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kpfu.itis.model.entity.RegisterVerificationToken;

public interface RegisterTokensRepository extends JpaRepository<RegisterVerificationToken, Long> {

    RegisterVerificationToken findByToken(String token);
}
