package ru.kpfu.itis.security.cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kpfu.itis.model.entity.RememberMeToken;
import ru.kpfu.itis.repository.LoginTokensRepository;

import java.util.Date;

@Repository("hibernatePersistentTokenRepository")
@Transactional
public class HibernateTokenRepositoryImpl implements PersistentTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(HibernateTokenRepositoryImpl.class);

    @Autowired
    private LoginTokensRepository loginTokensRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {

        RememberMeToken rememberMeToken = new RememberMeToken();

        rememberMeToken.setUsername(token.getUsername());
        rememberMeToken.setSeries(token.getSeries());
        rememberMeToken.setToken(token.getTokenValue());
        rememberMeToken.setLast_used(token.getDate());

        loginTokensRepository.save(rememberMeToken);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String series) {

        RememberMeToken rememberMeToken = loginTokensRepository.findBySeries(series);

        if (rememberMeToken == null) return null;

        return new PersistentRememberMeToken(rememberMeToken.getUsername(),
                rememberMeToken.getSeries(), rememberMeToken.getToken(), rememberMeToken.getLast_used());
    }

    @Override
    public void removeUserTokens(String username) {

        RememberMeToken rememberMeToken = loginTokensRepository.findByUsername(username);

        if (rememberMeToken != null) loginTokensRepository.delete(rememberMeToken);
    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {

        RememberMeToken rememberMeToken = loginTokensRepository.findBySeries(seriesId);

        rememberMeToken.setToken(tokenValue);
        rememberMeToken.setLast_used(lastUsed);

        loginTokensRepository.save(rememberMeToken);
    }

}