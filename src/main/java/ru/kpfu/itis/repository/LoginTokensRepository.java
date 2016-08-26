package ru.kpfu.itis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.model.entity.RememberMeToken;

@Repository
public interface LoginTokensRepository extends JpaRepository<RememberMeToken, String> {

    RememberMeToken findBySeries(String series);

    RememberMeToken findByUsername(String username);
}
