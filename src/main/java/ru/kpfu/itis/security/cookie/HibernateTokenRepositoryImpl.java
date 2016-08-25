package ru.kpfu.itis.security.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.entity.PersistentLogin;
import ru.kpfu.itis.repository.PersistentLoginRepository;

import java.util.Date;

@Repository("hibernatePersistentTokenRepository")
@Transactional
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);

    @Autowired
    private PersistentLoginRepository persistentLoginRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        PersistentLogin persistentLogin = new PersistentLogin();

        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());

        persistentLoginRepository.save(persistentLogin);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {

        PersistentLogin persistentLogin = persistentLoginRepository.findBySeries(series);

        if (persistentLogin == null) return null;

        return new PersistentRememberMeToken(persistentLogin.getUsername(),
                persistentLogin.getSeries(), persistentLogin.getToken(), persistentLogin.getLast_used());
    }

    @Override
    public void removeUserTokens(String username) {

        PersistentLogin persistentLogin = persistentLoginRepository.findByUsername(username);

        if (persistentLogin != null) persistentLoginRepository.delete(persistentLogin);
    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {

        PersistentLogin persistentLogin = persistentLoginRepository.findBySeries(seriesId);

        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);

        persistentLoginRepository.save(persistentLogin);
    }

}